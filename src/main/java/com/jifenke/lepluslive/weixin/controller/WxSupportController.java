package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.weixin.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 提供微信相关接口
 * Created by zhangwen on 2017/10/17.
 */
@RestController
@CrossOrigin
@RequestMapping("/wx")
public class WxSupportController {

    @Autowired
    private WeiXinService weiXinService;

    @RequestMapping(value = "/jsconfig", method = RequestMethod.GET)
    public Map getJsConfig(HttpServletRequest request) {
        return weiXinService.getWeiXinConfig(request);
    }
}
