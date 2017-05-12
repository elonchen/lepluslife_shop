package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.domain.entities.Dictionary;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  /**
   * 进入合伙人页面
   */
  @RequestMapping(value = "/tt/becomePartner", method = RequestMethod.GET)
  public ModelAndView goBecomePartnerPage(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
//    WeiXinUser weiXinUser = weiXinService.(request);
    Optional partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
    if (partnerByWeiXinUser.isPresent()) {
      return MvUtil.go("/partner/register/alreadyPartner");
    } else {
      Long current = onlineOrderService.sumOrderPriceByLeJiaUser(weiXinUser.getLeJiaUser().getId());
      Long limit = Long.valueOf(dictionaryService.findDictionaryById(59L).getValue());
      if (current >= limit) {
        return MvUtil.go("/order/register/permitRegister");
      } else {
        return MvUtil.go("/order/register/forbidRegister");
      }
    }

  }

}
