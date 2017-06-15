package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.activity.service.RechargeService;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;

import org.jdom.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机充值 Created by zhangwen on 2016/10/26.
 */
@RestController
@RequestMapping("/front/phone")
public class RechargeController {

  @Inject
  private MessageService messageUtil;

  @Inject
  private RechargeService rechargeService;

  @Inject
  private ActivityPhoneOrderService orderService;

  /**
   * 检查当前是否可以下单，以及下单价格  16/10/26
   *
   * @param phone 手机号码
   * @param worth 充值面值
   */
  @RequestMapping(value = "/check", method = RequestMethod.GET)
  public LejiaResult check(@RequestParam String phone, @RequestParam Integer worth) {

    Map result = rechargeService.check(phone, worth);
    if (result != null) {
      System.out.println(result.toString());
      if ("success".equals(result.get("status"))) {
        return LejiaResult.ok();
      } else {
        return LejiaResult.build(500, "" + result.get("message"));
      }
    }
    return LejiaResult.build(500, messageUtil.getMsg("500"));
  }

  /**
   * 充值回调函数 16/10/28
   */
  @RequestMapping(value = "/afterPay", method = RequestMethod.GET)
  public void afterPay(HttpServletRequest request, HttpServletResponse response)
      throws IOException, JDOMException {
    String result = "success";
    Map<String, String[]> map = request.getParameterMap();
    System.out.println(map.toString());
    String status = map.get("status")[0];
    String orderSid = map.get("sp_order_id")[0];

    if ("success".equalsIgnoreCase(status)) {
      String orderId = map.get("order_id")[0];
      Integer worth = Integer.valueOf(map.get("worth")[0]);
      String price = map.get("price")[0];
      String sign = map.get("sign")[0];
      try {//验证签名
        ActivityPhoneOrder
            order =
            orderService.checkSign(orderId, status, price, worth, orderSid, sign);

        if (order != null && order.getState() != 2) {//处理订单

          Integer p = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
          orderService.paySuccess(order, orderId, p, worth);
        } else {
          result = "failed";
        }
      } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("failed");
      }
    } else {
      //处理失败，对订单进行标识
      String message = map.get("message") != null ? map.get("message")[0] : "未知错误1";
      try {
        orderService.payFail(orderSid, message);
      } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("failed");
      }
    }
    //返回信息
    response.getWriter().write(result);
  }
}
