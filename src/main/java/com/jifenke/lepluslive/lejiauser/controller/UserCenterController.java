package com.jifenke.lepluslive.lejiauser.controller;

import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.controller.dto.LeJiaUserDto;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * 乐加用户相关操作
 *
 * @author zhangwen【zhangwenit@126.com】 2017/6/9 11:16
 **/
@RestController
@RequestMapping("/user")
public class UserCenterController {

  private static Logger logger = LoggerFactory.getLogger(UserCenterController.class);

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private MessageService messageUtil;

  @Inject
  private ScoreCService scoreCService;


  /**
   * APP微信登录  2017/6/9
   */
  @RequestMapping(value = "/sign/login", method = RequestMethod.POST)
  public LejiaResult wxLogin(@RequestParam String unionid,
                             @RequestParam String openid,
                             @RequestParam String country,
                             @RequestParam String nickname,
                             @RequestParam String city,
                             @RequestParam String province,
                             @RequestParam String headimgurl,
                             @RequestParam String language,
                             @RequestParam Long sex,
                             @RequestParam String token) {
    if (unionid == null || "null".equals(unionid) || "".equals(unionid)) {
      return LejiaResult.build(2008, messageUtil.getMsg("2008"));
    }
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByUnionId(unionid);  //是否已注册
    LeJiaUser leJiaUser = null;
    try {
      leJiaUser =
          weiXinUserService
              .saveWeiXinUserByApp(weiXinUser, unionid, openid, country, city, nickname,
                                   province, language, headimgurl, sex, token);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "服务器异常");
    }
    ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
    if (scoreA != null && scoreC != null && leJiaUser != null) {
      return LejiaResult.build(200, "登录成功", new LeJiaUserDto(scoreA.getScore(), scoreC.getScore(),
                                                             leJiaUser.getUserSid(),
                                                             headimgurl,
                                                             nickname,
                                                             leJiaUser.getPhoneNumber()));
    } else {
      return LejiaResult.build(500, "服务器异常");
    }
  }

}
