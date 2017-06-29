package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.domain.entities.ActivityJoinLog;
import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.activity.service.ActivityCodeBurseService;
import com.jifenke.lepluslive.activity.service.ActivityJoinLogService;
import com.jifenke.lepluslive.activity.service.RechargeCardService;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.domain.entities.Verify;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.lejiauser.service.ValidateCodeService;
import com.jifenke.lepluslive.lejiauser.service.VerifyService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 活动 Created by zhangwen on 16/9/2.
 */
@RestController
@RequestMapping("/weixin")
public class ActivityCodeBurseController {

  @Inject
  private ActivityCodeBurseService activityCodeBurseService;
  @Inject
  private WeiXinUserService weiXinUserService;
  @Inject
  private ActivityJoinLogService activityJoinLogService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private MessageService messageService;

  @Inject
  private RechargeCardService rechargeCardService;

  @Inject
  private ValidateCodeService validateCodeService;

  @Inject
  private VerifyService verifyService;

  //关注图文链接页面
  @RequestMapping("/subPage")
  public ModelAndView subPage(HttpServletRequest request, Model model,
                              @RequestParam(required = false) String subSource) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    //判断是否获得过红包
    ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser);
    if (joinLog == null) {//未参与
      if (weiXinUser.getLeJiaUser().getPhoneNumber() != null && !""
          .equals(weiXinUser.getLeJiaUser().getPhoneNumber())) {
        model.addAttribute("status", 1);
//        model.addAttribute("scoreA", 200);
      } else {
        model.addAttribute("status", 0);
      }
    } else {
//      model.addAttribute("scoreA", joinLog.getDetail());
      model.addAttribute("status", 1);
    }
    //发送验证码限制
    Verify verify = verifyService.addVerify(weiXinUser.getLeJiaUser().getId(), 18005);
    model.addAttribute("pageSid", verify.getPageSid());
    model.addAttribute("subSource", subSource);
    return MvUtil.go("/activity/subPage2");
  }

  /**
   * 关注图文链接页面 输入手机号,点击领取红包 16/09/20
   *
   * @param phoneNumber 手机号
   */
  @RequestMapping(value = "/subPage/open")
  public LejiaResult subPageOpen(@RequestParam String phoneNumber, @RequestParam String code,
                                 HttpServletRequest request,
                                 @RequestParam(required = false) String subSource) {
    Boolean b = validateCodeService.findByPhoneNumberAndCode(phoneNumber, code); //验证码是否正确
    if (!b) {
      return LejiaResult.build(3001, "验证码错误");
    }
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
    ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser);
    if (leJiaUser != null && !weiXinUser.getLeJiaUser().getId().equals(leJiaUser.getId())) {
      leJiaUser.setPhoneNumber(null);
      leJiaUserService.saveUser(leJiaUser);
    }
    if (joinLog == null) {
      //判断是否需要绑定合伙人 1_0_3
      leJiaUserService.checkUserBindPartner(weiXinUser.getLeJiaUser(), subSource);

      //派发红包和金币,填充手机号码成为会员
      try {
        Map<String, Integer> map = weiXinUserService.giveScoreAByDefault(weiXinUser, phoneNumber);

        //添加参加记录
        activityJoinLogService.addCodeBurseLogByDefault(weiXinUser, map.get("scoreC"));
        return LejiaResult.ok(map);
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "服务器异常");
      }
    }
    return LejiaResult.build(6005, messageService.getMsg("6005"));
  }

  //关注永久二维码领取红包
  @RequestMapping(value = "/activity/{id}", method = RequestMethod.GET)
  public ModelAndView goActivityPage(HttpServletRequest request, @PathVariable String id,
                                     Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    String[] str = id.split("_");
    if ("0".equals(str[0])) { //普通关注
      //判断是否获得过红包
      ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser);
      int defaultScoreA = Integer.valueOf(dictionaryService.findDictionaryById(18L).getValue());
      if (joinLog == null) {//未参与
        //派发红包,获取默认派发红包金额
        int
            status =
            scoreAService
                .giveScoreAByDefault(weiXinUser.getLeJiaUser(), defaultScoreA, "关注送鼓励金", 0,
                                     "0_" + defaultScoreA);
        //添加参加记录
        if (status == 1) {
          activityJoinLogService.addCodeBurseLogByDefault(weiXinUser, defaultScoreA);
        }
      }
      model.addAttribute("singleMoney", defaultScoreA);
      model.addAttribute("status", 200);
    } else {
      //判断活动是否失效
      ActivityCodeBurse
          codeBurse =
          activityCodeBurseService.findCodeBurseById(Long.valueOf(str[1]));
      if (codeBurse != null) {
        //活动优先级最高(已结束||暂停||派发完毕)
        if (codeBurse.getEndDate().getTime() < new Date().getTime() || codeBurse.getState() == 0
            || codeBurse.getBudget().intValue() < codeBurse.getTotalMoney().intValue()) {
          model.addAttribute("status", 201);
          model.addAttribute("singleMoney", codeBurse.getSingleMoney());
        } else {
          //判断是否参与过该种活动
          ActivityJoinLog
              joinLog =
              activityJoinLogService
                  .findLogBySubActivityAndOpenId(codeBurse.getType(), weiXinUser);
          if (joinLog == null) {//未参与
            int status = scoreAService.giveScoreAByActivity(codeBurse, weiXinUser.getLeJiaUser());
            //添加参加记录
            if (status == 1) {
              activityJoinLogService.addCodeBurseLog(codeBurse, weiXinUser);
            }
            model.addAttribute("singleMoney", codeBurse.getSingleMoney());
          } else {
            model.addAttribute("singleMoney", joinLog.getDetail());
          }
          model.addAttribute("status", 200);
        }
      } else {
        model.addAttribute("status", 404);
      }
    }
    // model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    return MvUtil.go("/activity/codeBurse");
  }

  /**
   * 跳转到当前临时活动页面  16/10/18
   *
   * @param id 活动版本
   */
  @RequestMapping(value = "/activity/short/{id}", method = RequestMethod.GET)
  public void shortPage(HttpServletResponse response, @PathVariable String id) {
    try {
      response.sendRedirect("/resource/frontRes/activity/short/version" + id + "/index.html");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 临时活动页面加载访问  16/10/18
   *
   * @param version 活动对应的版本
   */
  @RequestMapping(value = "/activity", method = RequestMethod.POST)
  public LejiaResult activityPage(@RequestParam Long version, HttpServletRequest request) {
    WeiXinUser user = weiXinService.getCurrentWeiXinUser(request);
    ActivityJoinLog joinLog = activityJoinLogService.findLogByTypeAndUser(4, version, user);
    Map<Object, Object> result = new HashMap<>();
    result.put("user", user);
    if (joinLog != null) {
      result.put("status", 1);
    } else {
      result.put("status", 0);
    }
    return LejiaResult.ok(result);
  }

  /**
   * 临时活动页面加载访问  16/10/18
   *
   * @param version 活动对应的版本
   * @param scoreA  发放的红包
   * @param scoreB  发放的积分
   */
  @RequestMapping(value = "/short/submit", method = RequestMethod.POST)
  public LejiaResult activitySubmit(@RequestParam Long version, @RequestParam Integer scoreA,
                                    @RequestParam Integer scoreB, @RequestParam String aInfo,
                                    @RequestParam String bInfo, HttpServletRequest request) {
    WeiXinUser user = weiXinService.getCurrentWeiXinUser(request);
    ActivityJoinLog joinLog = activityJoinLogService.findLogByTypeAndUser(4, version, user);
    Map<Object, Object> result = new HashMap<>();
    result.put("user", user.getLeJiaUser());
    if (joinLog != null) {
      result.put("status", 1);
    } else {
      //给红包和积分，并添加领取记录
      try {
        Map<Object, Object> map = weiXinUserService
            .shortActivitySubmit(user, scoreA, scoreB, aInfo, bInfo, 11, 4 + "_" + version, version,
                                 4);
        result.put("map", map);

      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "服务器异常");
      }
    }
    return LejiaResult.ok(result);
  }

  /**
   * 充值卡 兑换
   *
   * @param exchangeCode 充值兑换码
   * @return 状态
   */
  @RequestMapping(value = "/rechargeCard/exchange", method = RequestMethod.POST)
  public LejiaResult rechargeCardSubmit(@RequestParam String exchangeCode,
                                        HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);

    try {
      if (exchangeCode != null && !exchangeCode.equals("")) {
        List<RechargeCard> list1 = rechargeCardService.findRechargeCardByExchangeCode(exchangeCode);
        List<RechargeCard> list2 = rechargeCardService.findRechargeCardByWeiXinUser(weiXinUser);
        if (list1.size() > 0) {
          return LejiaResult.build(499, "兑换码已使用!");
        }
        if (list2.size() > 100) {
          return LejiaResult.build(498, "兑换次数超限!");
        }

        RechargeCard rechargeCard = new RechargeCard();
        rechargeCard.setRechargeStatus(1);
        rechargeCard.setCreateTime(new Date());
        rechargeCard.setExchangeCode(exchangeCode);
        rechargeCard.setWeiXinUser(weiXinUser);
        rechargeCardService.saveRechargeCard(rechargeCard);
        return LejiaResult.ok();
      } else {

        return LejiaResult.build(497, "兑换码错误!");
      }

    } catch (Exception e) {
      return LejiaResult.build(202, "服务器异常");
    }
  }

}
