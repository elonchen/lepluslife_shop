package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/3/18.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinService {

  @Value("${weixin.appSecret}")
  private String secret;

  @Value("${weixin.appId}")
  private String appid;

  @Value("${weixin.grantType}")
  private String grantType;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinPayService weiXinPayService;

  public Map<String, Object> getSnsAccessToken(String code) {
    String
        getUrl =
        "https://api.weixin.qq.com/sns/oauth2/access_token?secret=" + secret + "&appid=" + appid
        + "&code=" + code + "&grant_type=" + grantType;
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    return HttpUtils.get(getUrl, headers);
  }

  public Map<String, Object> getDetailWeiXinUser(String accessToken, String openid) {
    String
        getUrl =
        "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json;charset=utf8mb4");
    return HttpUtils.get(getUrl, headers);
  }

  //主动获取微信用户信息
  public Map<String, Object> getWeiXinUserInfo(String openid) {
    String accessToken = dictionaryService.findDictionaryById(7L).getValue();
    String
        getUrl =
        "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid="
        + openid;
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json;charset=utf8mb4");
    return HttpUtils.get(getUrl, headers);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser getCurrentWeiXinUser(HttpServletRequest request) {
    String unionId = CookieUtils.getCookieValue(request, "leJiaShopUnionId");
    return weiXinUserService.findWeiXinUserByUnionId(unionId);
  }

  @Transactional(readOnly = true)
  public boolean checkWeiXinRequest(String signature, String timestamp, String nonce) {
    String[] strs = new String[]{Constants.WEI_XIN_TOKEN, timestamp, nonce};
    Arrays.sort(strs);
    String sign = strs[0] + strs[1] + strs[2];
    return DigestUtils.sha1Hex(sign).equals(signature);
  }

  /**
   * 获取页面调用接口所需的配置参数wxConfig
   */
  public Map getWeiXinConfig(HttpServletRequest request) {
    Long timestamp = new Date().getTime() / 1000;
    String noncestr = MvUtil.getRandomStr();
    Map<String, Object> map = new HashMap<>();
    map.put("appId", appid);
    map.put("timestamp", timestamp);
    map.put("noncestr", noncestr);
    map.put("signature", weiXinPayService.getJsapiSignature(request, noncestr, timestamp));
    return map;
  }
}
