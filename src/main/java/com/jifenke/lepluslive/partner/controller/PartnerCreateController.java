package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.lejiauser.service.ValidateCodeService;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 2017/5/12.
 */
@RestController
@RequestMapping("/front/partner")
public class PartnerCreateController {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private OnlineOrderService onlineOrderService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinUserRepository weiXinUserRepository;

  @Inject
  private ValidateCodeService validateCodeService;


  /**
   * 进入合伙人页面
   */
  @RequestMapping(value = "/weixin/becomePartner", method = RequestMethod.GET)
  public ModelAndView goBecomePartnerPage(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
//    WeiXinUser weiXinUser = weiXinUserRepository.findOne(1L);
    Optional partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
    if (partnerByWeiXinUser.isPresent()) {
      model.addAttribute("weiXinUser", weiXinUser);
      return MvUtil.go("/partner/register/alreadyPartner");
    } else {
      Long current = onlineOrderService.sumOrderPriceByLeJiaUser(weiXinUser.getLeJiaUser().getId());
      Long limit = Long.valueOf(dictionaryService.findDictionaryById(59L).getValue());
      if (current >= limit) {
        model.addAttribute("limit", limit);
        return MvUtil.go("/partner/register/permitRegister");
      } else {
        model.addAttribute("current", current);
        model.addAttribute("limit", limit);
        return MvUtil.go("/partner/register/forbidRegister");
      }
    }
  }

  /**
   * 进入合伙人注册页面
   */
  @RequestMapping(value = "/weixin/register", method = RequestMethod.GET)
  public ModelAndView partnerRegisterPage(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
//    WeiXinUser weiXinUser = weiXinUserRepository.findOne(1L);
    model.addAttribute("weiXinUser", weiXinUser);
    return MvUtil.go("/partner/register/register");
  }

  /**
   * 合伙人注册接口
   */
  @RequestMapping(value = "/weixin/doRegister", method = RequestMethod.POST)
  public LejiaResult partnerRegister(@RequestParam(required = false) String name,@RequestParam(required = false) String phoneNumber,
                                      @RequestParam(required = false) String code,HttpServletRequest request) {
    try {
      Boolean b = validateCodeService.findByPhoneNumberAndCode(phoneNumber, code); //验证码是否正确
      if(b){
            WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
//        WeiXinUser weiXinUser = weiXinUserRepository.findOne(1L);
         partnerService.registerPartner(phoneNumber, name, weiXinUser); //创建合伙人
        return LejiaResult.build(200, "注册成功");
      }else {
        return LejiaResult.build(3001, "验证码错误");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "服务器异常");
    }
  }

  /**
   * 合伙人注册成功接口
   */
  @RequestMapping(value = "/weixin/register_success", method = RequestMethod.POST)
  public ModelAndView goRegisterSuccessPage(Model model,HttpServletRequest request) {
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
//    WeiXinUser weiXinUser = weiXinUserRepository.findOne(1L);
    model.addAttribute("weiXinUser", weiXinUser);
    return MvUtil.go("/partner/register/registerSuccess");
  }
}
