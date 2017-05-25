package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信用户相关
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/24 16:04
 **/
@RestController
@RequestMapping("/front/weixin")
public class WeiXinUserController {

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private LeJiaUserService leJiaUserService;

  /**
   * 订单页面将该用户变为会员  2017/05/24
   */
  @RequestMapping(value = "/weixin/register", method = RequestMethod.POST)
  public LejiaResult register(HttpServletRequest request, @RequestParam String phoneNumber) {

    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);

    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);
    if (leJiaUser == null) {
      weiXinUserService.setWeiXinStateAndPhone(weiXinUser, phoneNumber);
    }
    return LejiaResult.ok();
  }

}
