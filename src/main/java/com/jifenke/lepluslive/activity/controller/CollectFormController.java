package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.service.CollectFormService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 收集表单【各种页面信息提交】
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/16 13:46
 **/
@RestController
@RequestMapping("/front/form")
public class CollectFormController {

  @Inject
  private CollectFormService collectFormService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private PartnerService partnerService;


  /**
   * 邀请商户入驻  2017/5/16
   */
  @RequestMapping(value = "/weixin/inviteM", method = RequestMethod.GET)
  public ModelAndView inviteMerchant(HttpServletRequest request, Model model) {

    model.addAttribute("partner", partnerService
        .findPartnerByWeiXinUser(weiXinService.getCurrentWeiXinUser(request)).orElse(null));

    return MvUtil.go("/partner/invite/merchant");
  }

  /**
   * 邀请商户入驻  2017/5/16
   */
  @RequestMapping(value = "/submit", method = RequestMethod.POST)
  public LejiaResult submit(Long type, String content) {

    try {
      collectFormService.insertForm(type, content);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "save error");
    }

    return LejiaResult.ok();
  }

}
