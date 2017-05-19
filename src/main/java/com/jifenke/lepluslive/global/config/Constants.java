package com.jifenke.lepluslive.global.config;

/**
 * Application constants.
 */
public final class Constants {

  // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
  public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
  public static final String SPRING_PROFILE_PRODUCTION = "prod";
  public static final String SPRING_PROFILE_FAST = "fast";
  // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
  public static final String SPRING_PROFILE_CLOUD = "cloud";

  public static final String WEI_XIN_TOKEN = "88zctest";

  public static final Long CODE_EXPIRE_SECONDS = 2592000L; //公众号临时二维码过期时间 30天

  public static final String APPID = "wx9a694d7de8b8b081";

  public static final String WEI_XIN_URL = "http://shop.tiegancrm.com";

//  public static final String WEI_XIN_LIFE_URL = "http://www.tiegancrm.com"; //测试
  public static final String WEI_XIN_LIFE_URL = "http://www.lepluslife.com"; //线上


  public static final String CARD_CHECK_KEY = "8001808eb38443d35671c7c6f8c7ddc0";
  public static final String
      CARD_CHECK_URL =
      "http://e.apix.cn/apixcredit/bankcardinfo/bankcardinfo?cardno=";

  public static final Long ORDER_EXPIRED = 900000L;

  public static final Long VALIDATECODE_EXPIRED = 300000L;  //验证码过期时间

  public static final Integer COOKIE_DISABLE_TIME = 604800;

  public static final String SMS_SEND_URL = "https://eco.taobao.com/router/rest";
  public static final String SMS_REGISTER_CODE = "SMS_9605067";  //注册模板id
  public static final String SMS_CHANGE_PWD_CODE = "SMS_8275440";  //修改密码模板id
  public static final String SMS_BANGDING_CODE = "SMS_8275446";  //绑定手机号码模板id

  public static final String MSG_SENDER = "214"; //银联商务分配的渠道号
  public static final String
      BANK_SIGN_URL =
      "https://mktos.chinaums.com/spservice/spenc/doReSign";//卡号转加密接口地址
  public static final String
      BANK_REGISTER_URL =
      "https://mktos.chinaums.com/spservice/spmember/process";//会员注册接口地址

  public static final String
      PHONE_STATUS =
      "http://api.huafeiduo.com/gateway.cgi?mod=order.phone.get&";
  //检查是否完成了充值
  public static final String
      PHONE_CHECK =
      "http://api.huafeiduo.com/gateway.cgi?mod=order.phone.check&";
  //充值前查询是否可充接口
  public static final String
      PHONE_BALANCE =
      "http://api.huafeiduo.com/gateway.cgi?mod=account.balance&";
  //查询账户余额接口
  public static final String
      PHONE_SUBMIT =
      "http://api.huafeiduo.com/gateway.cgi?mod=order.phone.submit&";
  //手机号充值接口
  public static final String
      PHONE_NOTIFY_URL =
      "http://www.lepluslife.com/front/phone/afterPay";
  //充值回调
//  public static final String PHONE_NOTIFY_URL = "http://www.tiegancrm.com/front/phone/afterPay";  //充值回调

  public static final String
      ONLINEORDER_NOTIFY_URL =
      "http://shop.tiegancrm.com/weixin/pay/afterPay";
  //商城订单微信回调
  public static final String
      PHONEORDER_NOTIFY_URL =
      "http://shop.tiegancrm.com/weixin/pay/afterPhonePay";
  //话费订单微信回调
//  public static final String ONLINEORDER_NOTIFY_URL = "http://www.tiegancrm.com/weixin/pay/afterPay";  //商城订单微信回调
//  public static final String PHONEORDER_NOTIFY_URL = "http://www.tiegancrm.com/weixin/pay/afterPhonePay";  //话费订单微信回调

  public static final String
      WEIXIN_QRCODE =
      "gQEr8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyUjhQN3BmM0NmVjExMDAwME0wN1gAAgRV7htZAwQAAAAA";
  //商品详情关注永久二维码

  public static final String
      WEIXIN_CODE_BACKIMG_URL =
      "http://image.lepluslife.com/leplus_shop/partner_code/back.png";
  //合成推广二维码背景图片地址

  public static final String
      WEIXIN_CODE_HEAD_URL =
      "http://image.lepluslife.com/leplus_shop/partner_code/back.png";
  //头像为空时的默认头像

  private Constants() {
  }
}
