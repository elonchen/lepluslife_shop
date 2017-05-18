package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerQrCode;
import com.jifenke.lepluslive.partner.service.PartnerQrCodeService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * 合伙人临时二维码
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/17 14:11
 **/
@RestController
@RequestMapping("/front/qrCode")
public class PartnerQrCodeController {

  @Inject
  private PartnerQrCodeService partnerQrCodeService;

  @Inject
  private PartnerService partnerService;

  /**
   * 商品详情页获取关注二维码
   */
  @RequestMapping(value = "/get", method = RequestMethod.POST)
  public LejiaResult getQrCode(@RequestParam(required = false) Long userId) {

    if (userId != null) {
      Partner partner = partnerService.findPartnerByWeiXinUser(new WeiXinUser(userId)).orElse(null);
      if (partner != null) {
        PartnerQrCode qrCode = partnerQrCodeService.findByPartner(partner);
        if (qrCode != null) {
          return LejiaResult.ok(qrCode.getTicket());
        }
      }
    }
    //返回一个永久二维码
    return LejiaResult.ok(Constants.WEIXIN_QRCODE);
  }

}
