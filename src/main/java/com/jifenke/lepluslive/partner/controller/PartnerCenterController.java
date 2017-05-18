package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.WeiXinWithdrawBill;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.WeiXinWithdrawBillService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Inject
    private WeiXinService weiXinService;


    /**
     * 合伙人中心 - 首页
     * 17/05/11
     * currPage
     */
    @RequestMapping(value = "/weixin/", method = RequestMethod.GET)
    public ModelAndView homePage(Model model, HttpServletRequest request, HttpServletResponse response) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
           try{
               response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
           }catch (Exception e) {
               e.printStackTrace();
           }
        }
        Long userCount = partnerService.findUserBindByPartner(partner);
        Long merchantCount = partnerService.findMerchantBindCountByPartner(partner);
        Long totalCommission = partnerService.findPartnerTotalCommisssion(partner);
        model.addAttribute("partner", partner);
        model.addAttribute("userCount", userCount);
        model.addAttribute("merchantCount", merchantCount);
        model.addAttribute("totalCommission", totalCommission);
        model.addAttribute("merchantLimit", partner.getMerchantLimit());
        model.addAttribute("userLimit", partner.getUserLimit());
        return MvUtil.go("/partner/partnerCenter/partnerCenter");
    }


    /**
     * 合伙人中心 -  我的会员
     * 17/05/11
     * currPage
     */
    @RequestMapping(value = "/weixin/myMember", method = RequestMethod.GET)
    public ModelAndView myBindUsers(Model model,HttpServletRequest request,HttpServletResponse response) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
            try{
                response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Integer currPage = 0;                                                                // Start
        Map result = partnerService.findUserByPartner(partner, currPage);
        model.addAttribute("data", result);
        model.addAttribute("partner", partner);
        return MvUtil.go("/partner/partnerCenter/myMember");
    }

    @RequestMapping(value = "/weixin/myMember/{currPage}", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult myBindUsersByPage(@PathVariable Integer currPage,HttpServletRequest request) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }
        List<Object[]> result = partnerService.findBindUsersByPage(partner, currPage * 10);
        return LejiaResult.ok(result);
    }

    /**
     * 合伙人中心 -  佣金记录
     * 17/05/12
     */
    @RequestMapping(value = "/weixin/commissionRecord", method = RequestMethod.GET)
    public ModelAndView myCommission(Model model, Integer currPage,HttpServletRequest request,HttpServletResponse response) {
        if (currPage == null) {
            currPage = 0;
        }
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
            try{
                response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map result = partnerService.findPartnerCommisssion(partner, currPage * 10);
        model.addAttribute("onLineLogs", result.get("onLineLogs"));
        model.addAttribute("offLineLogs", result.get("offLineLogs"));
        model.addAttribute("totalCommission", result.get("totalCommission"));
        return MvUtil.go("/partner/partnerCenter/commissionRecord");
    }

    @RequestMapping(value = "/weixin/commissionRecord/{currPage}", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult myCommissionByPage(@PathVariable Integer currPage,HttpServletRequest request) {
        if (currPage == null) {
            currPage = 0;
        }
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }
        Map result = partnerService.findPartnerCommisssionSimple(partner, currPage * 10);
        return LejiaResult.ok(result);
    }

    /**
     * 合伙人中心 -  我的好店
     * 17/05/12
     */
    @RequestMapping(value = "/weixin/myShops", method = RequestMethod.GET)
    public ModelAndView myCommission(Model model, MerchantCriteria merchantCriteria,HttpServletRequest request,HttpServletResponse response) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
            try{
                response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (merchantCriteria.getType() == null || merchantCriteria.getOrderBy() == null) {
            merchantCriteria = new MerchantCriteria();
            merchantCriteria.setOrderBy(0);
            merchantCriteria.setType(0);
        }
        merchantCriteria.setPartner(partner);
        List<Object[]> list = partnerService.findMerchantDataByPartner(merchantCriteria);
        Long totalBind = partnerService.findUserBindByPartner(partner);
        model.addAttribute("list", list);
        model.addAttribute("totalBind", totalBind);
        return MvUtil.go("/partner/partnerCenter/myShop");
    }

    @RequestMapping(value = "/weixin/myShopsByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult myCommissionByCriteria(Model model, MerchantCriteria merchantCriteria,HttpServletRequest request,HttpServletResponse response) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
            try{
                response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        merchantCriteria.setPartner(partner);
        List<Object[]> list = partnerService.findMerchantDataByPartner(merchantCriteria);
        return LejiaResult.ok(list);
    }

    /**
     * 合伙人中心  - 乐加客服
     * 17/05/15
     */
    @RequestMapping(value = "/weixin/customerService", method = RequestMethod.GET)
    public ModelAndView lejiaCustmerService(Model model,HttpServletRequest request) {
        model.addAttribute("servicePhone", dictionaryService.findDictionaryById(56L).getValue());
        return MvUtil.go("/partner/partnerCenter/customerService");
    }

    /**
     * 合伙人中心  - 提现中心
     * 17/05/15
     */
    @RequestMapping(value = "/weixin/withdrawCenter", method = RequestMethod.GET)
    public ModelAndView withdrawCenter(Model model,HttpServletRequest request,HttpServletResponse response) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
            try{
                response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Long totalCommission = partnerService.findPartnerTotalCommisssion(partner);
        model.addAttribute("totalCommission", totalCommission);
        return MvUtil.go("/partner/partnerCenter/withdrawCenter");
    }

    /**
     * 合伙人中心  - 发起提现
     * 17/05/15
     */
    @RequestMapping(value = "/weixin/withdrawCenter/withdraw", method = RequestMethod.POST)
    public LejiaResult doWithdraw(Long price,HttpServletRequest request) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }
        boolean result = weiXinWithdrawBillService.createWithdraw(partner, price);
        if(result) {
            return LejiaResult.ok();
        }else {
            return LejiaResult.build(500,"发起提现失败，请联系乐+客服！");
        }
    }



    /**
     * 合伙人中心  - 提现记录
     * 17/05/15
     */
    @RequestMapping(value = "/weixin/withdrawRecord", method = RequestMethod.GET)
    public ModelAndView wxWithdrawBill(Model model, Integer currPage,HttpServletRequest request,HttpServletResponse response) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }else {
            try{
                response.sendRedirect(request.getContextPath()+"/front/partner/weixin/becomePartner");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (currPage == null) {
            currPage = 0;
        }
        List<Object[]> list = weiXinWithdrawBillService.findByPartnerAndPage(partner.getId(), currPage * 10);
        Long partnerWd = weiXinWithdrawBillService.sumWithDrawByPartner(partner);
        Long wxWd = weiXinWithdrawBillService.sumWxWithDrawByPartner(partner);
        Long totalWithDraw = wxWd + partnerWd;
        model.addAttribute("bills", list);
        model.addAttribute("totalWithDraw", totalWithDraw);
        return MvUtil.go("/partner/partnerCenter/withdrawRecord");
    }

    @RequestMapping(value = "/weixin/withdrawRecordByPage/{currPage}", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult wxWithdrawBill(@PathVariable Integer currPage,HttpServletRequest request) {
        Partner partner = null;
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Optional<Partner> partnerByWeiXinUser = partnerService.findPartnerByWeiXinUser(weiXinUser);
        if(partnerByWeiXinUser.isPresent()) {
            partner = partnerByWeiXinUser.get();
        }
        List<Object[]> list = weiXinWithdrawBillService.findByPartnerAndPage(partner.getId(), currPage * 10);
        return LejiaResult.ok(list);
    }

}
