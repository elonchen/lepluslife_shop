package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.WeiXinWithdrawBill;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.WeiXinWithdrawBillService;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @Inject
    private WeiXinWithdrawBillService weiXinWithdrawBillService;


    /**
     * 合伙人中心 -  我的会员
     * 17/05/11
     *  currPage
     */
    @RequestMapping(value = "/myMember", method = RequestMethod.GET)
    public ModelAndView myBindUsers(Model model) {
        Partner partner = partnerService.findPartnerBySid("4354749");              // -- Temo
        Integer currPage=0;                                                                // Start
        Map result = partnerService.findUserByPartner(partner,currPage);
        model.addAttribute("data", result);
        return MvUtil.go("/partner/myMember/myMember");
    }
    @RequestMapping(value = "/myMember/{currPage}", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult myBindUsersByPage(@PathVariable Integer currPage) {
        Partner partner = partnerService.findPartnerBySid("4354749");              // -- Temo
        List<Object[]> result = partnerService.findBindUsersByPage(partner, currPage*10);
        return LejiaResult.ok(result);
    }

    /**
     *  合伙人中心 -  佣金记录
     *  17/05/12
     */
     @RequestMapping(value="/commissionRecord",method = RequestMethod.GET)
    public ModelAndView myCommission(Model model,Integer currPage) {
         if(currPage==null) {
             currPage = 0;
         }
         Partner partner = partnerService.findPartnerBySid("4354749");              // -- Temo
         Map result = partnerService.findPartnerCommisssion(partner,currPage*10);
         model.addAttribute("onlineLogs",result.get("onlineLogs"));
         model.addAttribute("offLineLogs",result.get("offLineLogs"));
         model.addAttribute("totalCommission",result.get("totalCommission"));
         return MvUtil.go("/partner/commissionRecord/commissionRecord");
     }
    @RequestMapping(value = "/commissionRecord/{currPage}", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult myCommissionByPage(@PathVariable Integer currPage) {
        if(currPage==null) {
            currPage = 0;
        }
        Partner partner = partnerService.findPartnerBySid("4354749");              // -- Temo
        Map result = partnerService.findPartnerCommisssionSimple(partner,currPage*10);
        return LejiaResult.ok(result);
    }
    /**
     *  合伙人中心 -  提现记录
     *  17/05/12
     */
    @RequestMapping(value="/partnerCenter",method = RequestMethod.GET)
    public ModelAndView myCommission(Model model, MerchantCriteria merchantCriteria) {
        Partner partner = partnerService.findPartnerBySid("4354749");              // -- Temo
        merchantCriteria.setPartner(partner);
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
        Partner partner = partnerService.findPartnerBySid("4354749");              // -- Temo
        currPage=10;
        List<WeiXinWithdrawBill> list = weiXinWithdrawBillService.findByPartnerAndPage(partner.getId(), currPage);
        model.addAttribute("bills",list);
        return MvUtil.go("/partner/withdrawRecord/withdrawRecord");
    }
}
