package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 线上订单 Created by zhangwen on 16/9/18.
 */
@RestController
@RequestMapping("/front/order")
public class OnlineOrderController {

  @Inject
  private OrderService orderService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private OnlineOrderService onlineOrderService;

  @Inject
  private ScoreCService scoreCService;

  /**
   * 订单确认页 16/09/22
   *
   * @param orderId 订单ID
   */
  @RequestMapping(value = "/weixin/confirmOrder", method = RequestMethod.GET)
  public ModelAndView productIndex(HttpServletRequest request, @RequestParam Long orderId,
                                   Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    OnLineOrder order = orderService.findOnLineOrderById(orderId);
    model.addAttribute("order", order);
    model.addAttribute("userState", weiXinUser.getState());
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    model.addAttribute("canUseScore",
                       scoreCService.findScoreCByLeJiaUser(weiXinUser.getLeJiaUser())
                           .getScore()); //用户可用金币
    if (order.getType() != null && order.getType() == 2) {
      return MvUtil.go("/gold/order/confirmOrder");
    }
    return MvUtil.go("/order/confirmOrder");
  }

  /**
   * 个人订单列表页 16/09/23
   */
  @RequestMapping(value = "/weixin/orderList")
  public ModelAndView goOrderListPage() {
    return MvUtil.go("/order/orderList");
  }

  /**
   * 分页获取用户各种类型订单 16/09/23
   */
  @RequestMapping(value = "/weixin/orderList", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult getCurrentUserAllOrder(HttpServletRequest request, @RequestParam Integer currPage,
                                     @RequestParam Integer state) {
    if (currPage == null || currPage < 1) {
      currPage = 1;
    }
    List<OnLineOrder>
        onLineOrders =
        onlineOrderService
            .getCurrentUserOrderListByPage(
                weiXinService.getCurrentWeiXinUser(request).getLeJiaUser(), state, currPage);
    return LejiaResult.ok(onLineOrders);
  }

  /**
   * 跳转到金币冲话费首页 17/02/22 在这儿的原因=微信支付目录设置问题
   */
  @RequestMapping(value = "/weixin/recharge", method = RequestMethod.GET)
  public ModelAndView recharge(HttpServletRequest request, Model model) {
    LeJiaUser leJiaUser = weiXinService.getCurrentWeiXinUser(request).getLeJiaUser();
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    model.addAttribute("phone", leJiaUser.getPhoneNumber());
    model.addAttribute("canUseScore",
                       scoreCService.findScoreCByLeJiaUser(leJiaUser).getScore()); //用户可用金币
    return MvUtil.go("/gold/recharge/index");
  }
}
