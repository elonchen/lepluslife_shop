package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by xf on 2017/5/11.
 * 合伙人中心
 */
@RestController
@RequestMapping("/partner")
public class PartnerController {

    @Inject
    private PartnerService partnerService;

    /**
     * 合伙人中心 -  我的会员
     * 17/05/11
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView myBindUsers(Model model) {
        Map result = partnerService.findUserByPartner(new Partner());
        model.addAttribute("data", result);
        return MvUtil.go("/");
    }

    /**
     *  合伙人中心 -  佣金记录
     *  17/05/12
     */
     @RequestMapping(value="/",method = RequestMethod.GET)
    public ModelAndView myCommission(Model model) {
         Map result = partnerService.findPartnerCommisssion(new Partner());
         model.addAttribute("onlineLogs",result.get("onlineLogs"));
         model.addAttribute("offLineLogs",result.get("offLineLogs"));
         model.addAttribute("totalCommission",result.get("totalCommission"));
         return MvUtil.go("/");
     }

    /**
     *  合伙人中心 -  提现记录
     *  17/05/12
     */
    @RequestMapping(value="/",method = RequestMethod.GET)
    public ModelAndView myCommission(Model model, MerchantCriteria merchantCriteria) {
        List<Object[]> list = partnerService.findMerchantDataByPartner(merchantCriteria);
        model.addAttribute(list);
        return MvUtil.go("/");
    }
}
