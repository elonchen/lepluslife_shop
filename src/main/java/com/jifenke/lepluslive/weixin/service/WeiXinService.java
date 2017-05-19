package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信接口相关
 * Created by zhangwen on 16/3/18.
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
    String accessToken = dictionaryService.findDictionaryById(57L).getValue();
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
    String unionId = CookieUtils.getCookieValue(request, "leJia_ShopUnionId");
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

  /**
   * 创建临时二维码  2017/5/12
   *
   * @param sceneId       场景ID
   * @param expireSeconds 过期时间 单位/秒  最大值=2592000(30天)
   * @return 二维码信息
   */
  public Map<String, Object> getQrCode(Long sceneId, Long expireSeconds) {
    String
        body =
        "{\"expire_seconds\": " + expireSeconds
        + ", \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + sceneId
        + "}}}";
    String
        postUrl =
        "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + dictionaryService
            .findDictionaryById(57L).getValue();
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");

    try {
      return HttpUtils.post(postUrl, headers, new StringEntity(body));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 上传临时图片素材，获取素材ID  2017/5/12
   *
   * @param image    图片缓冲
   * @param fileName 图片名称  .png
   * @return 二维码信息
   */
  public Map<String, Object> uploadImage(BufferedImage image, String fileName)
      throws IOException {

    String
        url =
        "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + dictionaryService
            .findDictionaryById(57L).getValue() + "&type=image";

    return HttpUtils.send(url, fileName, image);
  }

  /**
   * 客服接口，发送图片 2017/5/18
   *
   * @param openId  接受会员openId
   * @param mediaId 素材ID
   */
  public Map<String, Object> sendImg(String openId, String mediaId)
      throws IOException {
    String
        body =
        "{\"touser\":\"" + openId + "\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"" + mediaId
        + "\"}}";
    String
        url =
        "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + dictionaryService
            .findDictionaryById(57L).getValue();
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    return HttpUtils.post(url, headers, new StringEntity(body));
  }
}
