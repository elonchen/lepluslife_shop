package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
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
@RequestMapping("/front/partnerCenter")
public class PartnerCenterController {

    @Inject
    private PartnerService partnerService;
    @Inject
    private DictionaryService dictionaryService;

    /**
     * 合伙人中心 -  我的会员
     * 17/05/11
     *  currPage
     */
    @RequestMapping(value = "/myMember", method = RequestMethod.GET)
    public ModelAndView myBindUsers(Model model,Integer currPage) {
        Map result = partnerService.findUserByPartner(new Partner(),currPage);
        model.addAttribute("data", result);
        return MvUtil.go("/partner/myMember/myMember");
    }

    /**
     *  合伙人中心 -  佣金记录
     *  17/05/12
     */
     @RequestMapping(value="/commissionRecord",method = RequestMethod.GET)
    public ModelAndView myCommission(Model model,Integer currPage) {
         Map result = partnerService.findPartnerCommisssion(new Partner(),currPage);
         model.addAttribute("onlineLogs",result.get("onlineLogs"));
         model.addAttribute("offLineLogs",result.get("offLineLogs"));
         model.addAttribute("totalCommission",result.get("totalCommission"));
         return MvUtil.go("/partner/commissionRecord/commissionRecord");
     }

    /**
     *  合伙人中心 -  提现记录
     *  17/05/12
     */
    @RequestMapping(value="/partnerCenter",method = RequestMethod.GET)
    public ModelAndView myCommission(Model model, MerchantCriteria merchantCriteria) {
        List<Object[]> list = partnerService.findMerchantDataByPartner(merchantCriteria);
        model.addAttribute(list);
        return MvUtil.go("/partner/partnerCenter/partnerCenter");
    }
    /**
     *  合伙人中心  - 乐加客服
     *  17/05/15
     */
    @RequestMapping(value="/customerService",method = RequestMethod.GET)
    public ModelAndView lejiaCustmerService(Model model) {
        model.addAttribute("servicePhone",dictionaryService.findDictionaryById(56L).getValue());
        return MvUtil.go("/partner/customerService/customerService");
    }

    /**
     *  合伙人中心  - 点击提现
     *  17/05/15
     */


    /**
     *  合伙人中心  - 提现记录
     *  17/05/15
     */
    @RequestMapping(value="/withdrawRecord",method = RequestMethod.GET)
    public ModelAndView wxWithdrawBill(Model model,Integer currPage) {

        return MvUtil.go("/partner/withdrawRecord/withdrawRecord");
    }
}
