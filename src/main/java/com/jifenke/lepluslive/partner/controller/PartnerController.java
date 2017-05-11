package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
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

}
