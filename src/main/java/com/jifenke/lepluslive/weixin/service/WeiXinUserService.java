package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.activity.service.ActivityJoinLogService;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.score.repository.ScoreCRepository;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

/**
 * 微信用户
 * Created by zhangwen on 17/5/11.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinUserService {

  @Value("${bucket.ossBarCodeReadRoot}")
  private String barCodeRootUrl;

  @Inject
  private WeiXinUserRepository weiXinUserRepository;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private ScoreARepository scoreARepository;

  @Inject
  private ScoreBRepository scoreBRepository;

  @Inject
  private ScoreADetailRepository scoreADetailRepository;

  @Inject
  private LeJiaUserRepository leJiaUserRepository;

  @Inject
  private ScoreBDetailRepository scoreBDetailRepository;

  @Inject
  private ActivityJoinLogService joinLogService;

  @Inject
  private ScoreCRepository scoreCRepository;

  @Inject
  private WeiXinOtherUserService weiXinOtherUserService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private PartnerService partnerService;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser findWeiXinUserByOpenId(String openId) {
    List<WeiXinUser> list = weiXinUserRepository.findByOpenId(openId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser findWeiXinUserByUnionId(String unionId) {
    return weiXinUserRepository.findByUnionId(unionId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser findWeiXinUserByLeJiaUser(LeJiaUser leJiaUser) {
    return weiXinUserRepository.findByLeJiaUser(leJiaUser);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinUser(WeiXinUser weiXinUser) throws Exception {
    weiXinUserRepository.save(weiXinUser);
  }

  /**
   * 网页授权保存微信信息  2017/5/11
   *
   * @param userDetail 微信信息
   * @param user       微信其他账号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String[] saveWeiXinUser(Map<String, Object> userDetail, WeiXinOtherUser user)
      throws IOException {
    String openid = userDetail.get("openid").toString();
    String
        unionId =
        userDetail.get("unionid") != null ? userDetail.get("unionid").toString() : null;
    WeiXinUser weiXinUser = weiXinUserRepository.findByUnionId(unionId);
    ScoreA scoreA = null;
    ScoreB scoreB = null;
    Date date = new Date();
    String[] result = new String[2];

    if (weiXinUser == null) {
      result[0] = "null";
      weiXinUser = new WeiXinUser();
      weiXinUser.setLastUpdated(date);
      weiXinUser.setDateCreated(date);
      LeJiaUser leJiaUser = new LeJiaUser();
      leJiaUser.setHeadImageUrl(String.valueOf(userDetail.get("headimgurl")));
      leJiaUser.setWeiXinUser(weiXinUser);
      RegisterOrigin registerOrigin = new RegisterOrigin();
      registerOrigin.setId(1L);
      leJiaUser.setRegisterOrigin(registerOrigin);
      leJiaUserRepository.save(leJiaUser);
      weiXinUser.setLeJiaUser(leJiaUser);
      scoreA = new ScoreA();
      scoreA.setScore(0L);
      scoreA.setTotalScore(0L);
      scoreA.setLeJiaUser(leJiaUser);
      scoreARepository.save(scoreA);
      scoreB = new ScoreB();
      scoreB.setScore(0L);
      scoreB.setTotalScore(0L);
      scoreB.setLeJiaUser(leJiaUser);
      scoreBRepository.save(scoreB);
      ScoreC scoreC = new ScoreC();
      scoreC.setLeJiaUser(leJiaUser);
      scoreC.setLastUpdateDate(date);
      scoreCRepository.save(scoreC);
    } else if (weiXinUser.getOpenId() == null) {
      result[0] = "null";
    } else {
      result[0] = "unNull";
    }
    if (user == null) {
      user = new WeiXinOtherUser();
      user.setOpenId(openid);
      user.setWeiXinUser(weiXinUser);
      weiXinOtherUserService.saveWeiXinOtherUser(user);
    }

    weiXinUser.setUnionId(unionId);
    weiXinUser.setCity(String.valueOf(userDetail.get("city")));
    weiXinUser.setCountry(String.valueOf(userDetail.get("country")));
    weiXinUser.setSex(Long.parseLong(userDetail.get("sex").toString()));
    weiXinUser.setNickname(String.valueOf(userDetail.get("nickname")));
    weiXinUser.setLanguage(String.valueOf(userDetail.get("language")));
    weiXinUser.setHeadImageUrl(String.valueOf(userDetail.get("headimgurl")));
    weiXinUser.setProvince(String.valueOf(userDetail.get("province")));
    weiXinUser.setLastUserInfoDate(date);
    weiXinUser.setLastUpdated(date);
    weiXinUserRepository.save(weiXinUser);
    result[1] = unionId;
    return result;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinUserBySubscribe(Map<String, Object> userDetail)
      throws IOException {
    String openid = userDetail.get("openid").toString();
    WeiXinOtherUser user = weiXinOtherUserService.findByOpenId(openid);
    saveWeiXinUser(userDetail, user);
    if (user == null) {
      user = weiXinOtherUserService.findByOpenId(openid);
    }

    user.setSubState(1);
    if (user.getSubDate() == null) {
      user.setSubDate(new Date());
    }

    if (user.getSubSource() == null || "".equals(user.getSubSource())) {
      user.setSubSource(userDetail.get("subSource").toString());
    }

    weiXinOtherUserService.saveWeiXinOtherUser(user);
  }

  /**
   * app的微信登录
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LeJiaUser saveWeiXinUserByApp(WeiXinUser weiXinUser, String unionId, String openid,
                                       String country, String city, String nickname,
                                       String province,
                                       String language, String headImgUrl, Long sex, String token)
      throws IOException {
    ScoreA scoreA = null;
    ScoreB scoreB = null;
    LeJiaUser leJiaUser = null;
    Date date = new Date();
    if (weiXinUser == null) {
      weiXinUser = new WeiXinUser();
      weiXinUser.setLastUpdated(date);
      leJiaUser = new LeJiaUser();
      leJiaUser.setHeadImageUrl(headImgUrl);
      leJiaUser.setToken(token);
      leJiaUser.setWeiXinUser(weiXinUser);
      RegisterOrigin registerOrigin = new RegisterOrigin();
      registerOrigin.setId(2L);
      leJiaUser.setRegisterOrigin(registerOrigin);
      leJiaUserRepository.save(leJiaUser);
      weiXinUser.setLeJiaUser(leJiaUser);
      scoreA = new ScoreA();
      scoreA.setScore(0L);
      scoreA.setTotalScore(0L);
      scoreA.setLeJiaUser(leJiaUser);
      scoreARepository.save(scoreA);
      scoreB = new ScoreB();
      scoreB.setScore(0L);
      scoreB.setTotalScore(0L);
      scoreB.setLeJiaUser(leJiaUser);
      scoreBRepository.save(scoreB);
      ScoreC scoreC = new ScoreC();
      scoreC.setLeJiaUser(leJiaUser);
      scoreC.setLastUpdateDate(date);
      scoreCRepository.save(scoreC);
    } else {
      leJiaUser = weiXinUser.getLeJiaUser();
    }

    weiXinUser.setAppOpenId(openid);
    weiXinUser.setUnionId(unionId);
    weiXinUser.setCity(city);
    weiXinUser.setCountry(country);
    weiXinUser.setSex(sex);
    weiXinUser.setNickname(nickname);
    weiXinUser.setLanguage(language);
    weiXinUser.setHeadImageUrl(headImgUrl);
    weiXinUser.setProvince(province);
    weiXinUser.setLastUserInfoDate(date);
    weiXinUser.setLastUpdated(date);
    weiXinUser.setState(1);
    weiXinUserRepository.save(weiXinUser);

    return leJiaUser;
  }

  /**
   * 填充手机号送红包 16/09/20
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<String, Integer> giveScoreAByDefault(WeiXinUser weiXinUser, String phoneNumber)
      throws Exception {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
    String aRule = dictionaryService.findDictionaryById(34L).getValue(); //返A规则
    String bRule = dictionaryService.findDictionaryById(63L).getValue(); //返C规则
    int valueA = 0;
    int valueB = 0;
    Date date = new Date();
    Map<String, Integer> map = new HashMap<>();
    try {
      leJiaUser.setPhoneNumber(phoneNumber);
      leJiaUserRepository.save(leJiaUser);

      //是否返红包|返红包规则
      String[] aRules = aRule.split("_");
      if (!"0".equals(aRules[1])) {      //发红包
        int maxA = Integer.valueOf(aRules[1]);
        if ("0".equals(aRules[0])) {   //固定红包
          valueA = maxA;
        } else {//随机红包
          int minA = Integer.valueOf(aRules[0]);
          valueA = new Random().nextInt((maxA - minA) / 10) * 10 + minA;
        }
        scoreA.setScore(scoreA.getScore() + valueA);
        scoreA.setTotalScore(scoreA.getTotalScore() + valueA);
        scoreA.setLastUpdateDate(date);
        scoreARepository.save(scoreA);
        ScoreADetail scoreADetail = new ScoreADetail();
        scoreADetail.setNumber(Long.valueOf(String.valueOf(valueA)));
        scoreADetail.setScoreA(scoreA);
        scoreADetail.setOperate("注册送礼");
        scoreADetail.setOrigin(0);
        scoreADetail.setOrderSid("0_" + valueA);
        scoreADetailRepository.save(scoreADetail);
      }

      //是否返金币|返金币规则
      String[] bRules = bRule.split("_");
      if (!"0".equals(bRules[1])) {      //发金币
        int maxB = Integer.valueOf(bRules[1]);
        if ("0".equals(bRules[0])) {   //固定金币
          valueB = maxB;
        } else {//随机金币
          int minB = Integer.valueOf(bRules[0]);
          valueB = new Random().nextInt((maxB - minB) / 10) * 10 + minB;
        }
        scoreCService.saveScoreC(scoreC, 1, (long) valueB);
        scoreCService
            .saveScoreCDetail(scoreC, 1, (long) valueB, 0, "注册送礼",
                              "0_" + valueB);
      }
      weiXinUser.setState(1);
      weiXinUser.setStateDate(date);
      weiXinUserRepository.save(weiXinUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
    map.put("scoreA", valueA);
    map.put("scoreB", valueB);
    return map;
  }

  /**
   * 临时活动送红包和积分 16/10/18
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Map<Object, Object> shortActivitySubmit(WeiXinUser weiXinUser, Integer valueA,
                                                 Integer valueB, String aInfo, String bInfo,
                                                 Integer origin, String orderSid, Long activityId,
                                                 Integer type)
      throws Exception {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
    ScoreB scoreB = scoreBRepository.findByLeJiaUser(leJiaUser);
    Date date = new Date();
    Map<Object, Object> map = new HashMap<>();
    try {
      //是否返红包
      if (valueA != null && valueA > 0) {      //发红包
        scoreA.setScore(scoreA.getScore() + valueA);
        scoreA.setTotalScore(scoreA.getTotalScore() + valueA);
        scoreA.setLastUpdateDate(date);
        scoreARepository.save(scoreA);
        ScoreADetail scoreADetail = new ScoreADetail();
        scoreADetail.setNumber(Long.valueOf(String.valueOf(valueA)));
        scoreADetail.setScoreA(scoreA);
        scoreADetail.setOperate(aInfo);
        scoreADetail.setOrigin(origin);
        scoreADetail.setOrderSid(orderSid);
        scoreADetailRepository.save(scoreADetail);
      }

      //是否返积分
      if (valueB != null && valueB > 0) {      //发积分
        scoreB.setLastUpdateDate(date);
        scoreB.setScore(scoreB.getScore() + valueB);
        scoreB.setTotalScore(scoreB.getTotalScore() + valueB);
        scoreBRepository.save(scoreB);
        ScoreBDetail scoreBDetail = new ScoreBDetail();
        scoreBDetail.setNumber((long) valueB);
        scoreBDetail.setScoreB(scoreB);
        scoreBDetail.setOperate(bInfo);
        scoreBDetail.setOrigin(origin);
        scoreBDetail.setOrderSid(orderSid);
        scoreBDetailRepository.save(scoreBDetail);
      }
      weiXinUser.setState(1);
      weiXinUser.setStateDate(date);
      weiXinUserRepository.save(weiXinUser);

      //添加活动记录
      joinLogService.addLog(weiXinUser, valueA, valueB, activityId, type);
    } catch (Exception e) {
      e.printStackTrace();
    }
    map.put("scoreA", valueA);
    map.put("scoreB", valueB);
    return map;
  }

  /**
   * 将该用户变为会员  2017/02/23
   *
   * @param leJiaUser leJiaUser
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void setWeiXinState(LeJiaUser leJiaUser) {
    WeiXinUser w = findWeiXinUserByLeJiaUser(leJiaUser);
    if (w != null) {
      if (w.getState() == 0) {
        w.setState(1);
        weiXinUserRepository.save(w);
      }
    }
  }

  /**
   * 分享商品链接点击触发绑定流程  2017/05/23
   *
   * @param shareWxUserId 分享者的微信ID
   * @param clickWxUserId 点击者的微信ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void bindPartnerByShareProduct(Long shareWxUserId, Long clickWxUserId) {
    WeiXinUser shareUser = weiXinUserRepository.findOne(shareWxUserId);
    WeiXinUser clickUser = weiXinUserRepository.findOne(clickWxUserId);
    if (shareUser != null && clickUser != null) {
      Partner partner = partnerService.findPartnerByWeiXinUser(shareUser).orElse(null);
      if (partner != null) {
        Date date = new Date();
        LeJiaUser leJiaUser = clickUser.getLeJiaUser();
        long partnerUserLimit = leJiaUserRepository.countPartnerBindLeJiaUser(partner.getId());
        if (partner.getUserLimit() > partnerUserLimit) {
          //如果不是会员或没有绑定，切换绑定合伙人和虚拟商户
          if (clickUser.getState() == 0 || (leJiaUser.getBindMerchant() == null
                                            && leJiaUser.getBindPartner() == null)) {
            leJiaUser.setBindPartner(partner);
            leJiaUser.setBindPartnerDate(date);
            leJiaUser.setBindMerchant(new Merchant(1226L)); //统一虚拟商户
            leJiaUser.setBindMerchantDate(date);
            leJiaUserRepository.save(leJiaUser);
          } else if (leJiaUser.getBindMerchant() != null && leJiaUser.getBindPartner() == null
                     && partner.getId().equals(leJiaUser.getBindMerchant().getPartner().getId())) {
            //是会员，只绑定了门店，但门店合伙人和链接合伙人属于同一个,绑定合伙人
            leJiaUser.setBindPartner(partner);
            leJiaUser.setBindPartnerDate(date);
            leJiaUserRepository.save(leJiaUser);
          }
        }
      }
    }
  }

  /**
   * 订单页面将该用户变为会员  2017/05/24
   *
   * @param weiXinUser weiXinUser
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void setWeiXinStateAndPhone(WeiXinUser weiXinUser, String phone) {
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    Date date = new Date();
    weiXinUser.setState(1);
    weiXinUser.setStateDate(date);
    leJiaUser.setPhoneNumber(phone);
    leJiaUser.setPhoneBindDate(date);
    weiXinUserRepository.save(weiXinUser);
    leJiaUserRepository.save(leJiaUser);
  }
}
