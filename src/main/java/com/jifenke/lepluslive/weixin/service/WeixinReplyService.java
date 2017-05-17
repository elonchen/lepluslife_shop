package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.partner.service.PartnerQrCodeService;
import com.jifenke.lepluslive.weixin.domain.entities.AutoReplyRule;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinMessage;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReply;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReplyImageText;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReplyText;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/5/27.
 */
@Service
@Transactional(readOnly = true)
public class WeixinReplyService {

  @Inject
  private AutoReplyService autoReplyService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private WeixinMessageService weixinMessageService;

  @Inject
  private PartnerQrCodeService partnerQrCodeService;

  @Inject
  private WeiXinOtherUserService weiXinOtherUserService;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String routeWeixinEvent(Map map) {

    String str = "";
    switch ((String) map.get("Event")) {
      case "subscribe":
//                关注公众号的事件，包括手动关注和二维码关注两种
        str = buildFocusMessageReply(map, 1);
        break;
      case "unsubscribe"://取消关注公众号的事件
        unSubscribeWeiXinUser(map);
        break;
      case "MASSSENDJOBFINISH": //群发任务即将完成的时候，推送群发结果
        massEndJobFinish(map);
        break;
      case "SCAN":  //用户已关注后扫描带参数二维码
//                关注公众号的事件，包括手动关注和二维码关注两种
 //       str = buildFocusMessageReply(map, 2); //关注公众号后查询数据库有没有该用户信息，没有的话主动获取
        break;
      case "LOCATION":
        //上报地理位置
//        str = buildLocationReply(map);
        break;
      case "CLICK":
//                用户点击菜单事件
        str = buildMenuMessageReply(map);
//        menuViewVisit(map);
        break;
      case "VIEW":
//                点击菜单跳转链接时的事件推送
//        menuViewVisit(map);
        break;
      default:
        break;
    }
    return str;
  }

  public String routeWeixinMessage(Map map) {

    String str = "";
    switch ((String) map.get("MsgType")) {
      case "text":
//              用户在微信中发送文本消息
        str = buildTextMessageReply(map);
        break;
      case "image":
//              用户在微信中发送图片消息
        break;
      case "voice":
//                用户在微信中发送语音消息
        break;
      case "video":
//                用户在微信中发送视频消息
        break;
      case "location":
//                用户在微信中发送地理位置消息
        break;
      case "link":
//                用户在微信中发送链接消息
        break;
      default:
//                用户在微信中发送其他未定义的消息
        break;
    }
    return str;
  }

  /**
   * 关注公众号时发送的信息
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private String buildFocusMessageReply(Map map, Integer eventType) {
    WeiXinOtherUser
        user =
        weiXinOtherUserService.findByOpenId(map.get("FromUserName").toString());
    AutoReplyRule rule = autoReplyService.findByReplyType("focusReply");
    String str = "";
    String subSource = "0_0_0";
    if (rule != null) {
      WeixinReply reply = null;
      if (null != rule.getReplyText() && (!"".equals(rule.getReplyText()))) {
        reply = new WeixinReplyText(map, rule);
        str = reply.buildReplyXmlString(null);
      } else {
        reply = new WeixinReplyImageText(map, rule);
        HashMap<String, String> buildMap = new HashMap<>();
        //判断是不是临时二维码
        if (map.get("EventKey") != null && (!"".equals(map.get("EventKey").toString()))) {
          String eventKey = map.get("EventKey").toString().split("_")[1];
          if (eventKey.startsWith("Y")) {
            subSource = "2_0_0";
          } else {
            subSource = "1_0_" + eventKey;
          }
        }
        buildMap.put("title", "感谢您的关注，恭喜您获得臻品商城红包一个");
        buildMap.put("description", "↑↑↑戳这里，累计5000人领取");
        buildMap.put("url", Constants.WEI_XIN_URL + "/weixin/subPage?subSource=" + subSource);
        str = reply.buildReplyXmlString(buildMap);
      }
    }
    //关注公众号后查询数据库有没有该用户信息，没有的话主动获取
    subscribeWeiXinUser(map, subSource);
    return str;
  }

  /**
   * 点击自定义菜单发送图片
   */
  private String buildMenuMessageReply(Map map) {
    String mediaId = null;
    try {
      mediaId = partnerQrCodeService.getMediaId(map.get("FromUserName").toString());
    } catch (IOException e) {
      e.printStackTrace();
      return returnText(map, "系统异常，请稍后再试");
    }
    if (mediaId == null) { //未找到合伙人信息，返回文本消息
      return returnText(map, "请先注册成为合伙人");
    } else if (mediaId.startsWith("fail")) { //接口调用异常，返回文本消息
      return returnText(map, "接口调用异常，请稍后再试");
    }

    return returnImage(map, mediaId);
  }

  /**
   * 回复用户直接给公众号发送的信息
   */
  private String buildTextMessageReply(Map map) {
    AutoReplyRule rule = autoReplyService.findByKeyword((String) map.get("Content"));
    if (rule == null) {
      rule = autoReplyService.findByReplyType("defaultReply");
    }
    if (rule != null) {
      WeixinReply reply = null;
      if (null != rule.getReplyText() && (!"".equals(rule.getReplyText()))) {
        reply = new WeixinReplyText(map, rule);
      } else {
        reply = new WeixinReplyImageText(map, rule);
      }
      String str = reply.buildReplyXmlString(null);
      return str;
    }
    return "";
  }

  /**
   * 关注公众号后查询数据库有没有该用户信息，没有的话主动获取
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void subscribeWeiXinUser(Map map, String subSource) {
    String openId = map.get("FromUserName").toString();
    Map<String, Object> userDetail = weiXinService.getWeiXinUserInfo(openId);
    if (null == userDetail.get("errcode")) {
      try {
        userDetail.put("subSource", subSource);
        weiXinUserService.saveWeiXinUserBySubscribe(userDetail);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * 取消关注公众号后查询数据库有没有该用户信息，有的话取消关注
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void unSubscribeWeiXinUser(Map map) {
    String openId = map.get("FromUserName").toString();
    WeiXinOtherUser weiXinUser = weiXinOtherUserService.findByOpenId(openId);
    if (weiXinUser != null) {
      weiXinUser.setSubState(2);
      try {
        weiXinOtherUserService.saveWeiXinOtherUser(weiXinUser);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 群发任务即将完成的时候，推送群发结果 将结果保存到数据库
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void massEndJobFinish(Map map) {
    String msgID = map.get("MsgID").toString();
    WeixinMessage weixinMessage = weixinMessageService.findMessageByMsgID(msgID);
    if (weixinMessage != null) {
      try {
        weixinMessage.setToUserName(String.valueOf(map.get("ToUserName")));
        weixinMessage.setFromUserName(String.valueOf(map.get("FromUserName")));
        weixinMessage.setCreateTime(String.valueOf(map.get("CreateTime")));
        weixinMessage.setMsgType(String.valueOf(map.get("MsgType")));
        weixinMessage.setEvent(String.valueOf(map.get("Event")));
        weixinMessage.setStatus(String.valueOf(map.get("Status")));
        weixinMessage.setTotalCount(Integer.valueOf(String.valueOf(map.get("TotalCount"))));
        weixinMessage.setFilterCount(Integer.valueOf(String.valueOf(map.get("FilterCount"))));
        weixinMessage.setSentCount(Integer.valueOf(String.valueOf(map.get("SentCount"))));
        weixinMessage.setErrorCount(Integer.valueOf(String.valueOf(map.get("ErrorCount"))));
        weixinMessageService.saveNews(weixinMessage);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 回复文本消息  2017/5/15
   *
   * @param map 请求包
   * @param msg 消息内容
   */
  private String returnText(Map map, String msg) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<xml>");
    buffer.append("<ToUserName><![CDATA[" + map.get("FromUserName") + "]]></ToUserName>");
    buffer.append("<FromUserName><![CDATA[" + map.get("ToUserName") + "]]></FromUserName>");
    buffer.append("<CreateTime>" + map.get("CreateTime") + "</CreateTime>");
    buffer.append("<MsgType><![CDATA[text]]></MsgType>");
    buffer.append("<Content><![CDATA[" + msg + "]]></Content>");
    buffer.append("</xml>");
    return buffer.toString();
  }

  /**
   * 回复图片消息  2017/5/15
   *
   * @param map     请求包
   * @param mediaId 图片素材ID
   */
  private String returnImage(Map map, String mediaId) {
    //回复
    StringBuffer buffer = new StringBuffer();
    buffer.append("<xml><ToUserName><![CDATA[").append(map.get("FromUserName"))
        .append("]]></ToUserName>");
    buffer.append("<FromUserName><![CDATA[").append(map.get("ToUserName"))
        .append("]]></FromUserName>");
    buffer.append("<CreateTime>").append(map.get("CreateTime")).append("</CreateTime>");
    buffer.append("<MsgType><![CDATA[image]]></MsgType><Image><MediaId><![CDATA[");
    buffer.append(mediaId);
    buffer.append("]]></MediaId></Image></xml>");

    return buffer.toString();
  }

  //  /**
//   * 上报地理位置时发送的信息
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  private String buildLocationReply(Map map) {
//    // weiXinUserInfoService.saveWeiXinUserInfo(map);
//    return "success";
//  }

//  /**
//   * 点击菜单到URL时保存点击记录  2017/03/10
//   */
//  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//  private void menuViewVisit(Map map) {
//    visitLogService.saveLog(new VisitLog(String.valueOf(map.get("FromUserName")), "menu",
//                                         String.valueOf(map.get("EventKey")), "weixin"));
//    redisService.addClickLog(String.valueOf(map.get("FromUserName")), "menu:",
//                             String.valueOf(map.get("EventKey")));
//  }
}
