package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.DateUtils;
import com.jifenke.lepluslive.global.util.ImageUtil;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerQrCode;
import com.jifenke.lepluslive.partner.repository.PartnerQrCodeRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinOtherUserService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 合伙人临时二维码
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/12 10:37
 **/
@Service
@Transactional(readOnly = true)
public class PartnerQrCodeService {

  @Inject
  private PartnerQrCodeRepository repository;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private WeiXinOtherUserService weiXinOtherUserService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private DictionaryService dictionaryService;

  public PartnerQrCode findById(Long id) {
    return repository.findOne(id);
  }

  /**
   * 找出小于某个日期更新的二维码List 2017/5/18
   */
  public List<PartnerQrCode> listByDateUpdate(Date date) {
    return repository.findByDateUpdateLessThan(date);
  }

  /**
   * 获取二维码  2017/5/17
   *
   * @param partner 合伙人
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public PartnerQrCode findByPartner(Partner partner) {
    PartnerQrCode partnerQrCode = repository.findByPartner(partner).orElse(null);
    if (partnerQrCode == null) {
      partnerQrCode = insertQrCode(partner);
    } else if (DateUtils.getTimeStamp() - 2505600
               > partnerQrCode.getDateUpdate().getTime() / 1000) {
      //二维码已过期
      partnerQrCode = createScene(partnerQrCode);
      if (partnerQrCode == null) {
        return null;
      }
    }
    return partnerQrCode;
  }

  private void sendImg(String openId, Long id, int type) {

    System.out.println(openId + "====" + id + "======" + type);
    PartnerQrCode partnerQrCode = null;
    try {
      if (type == 1) {
        partnerQrCode = getQrCode(partnerService.findPartnerById(id));
      } else {
        partnerQrCode = repository.findOne(id);
        WeiXinUser weiXinUser = partnerQrCode.getPartner().getWeiXinUser();
        partnerQrCode =
            uploadImage(partnerQrCode, weiXinUser.getHeadImageUrl(), weiXinUser.getNickname());
      }
      //发送图片消息
      Map<String, Object>
          map =
          weiXinService.sendImg(openId, partnerQrCode.getMediaId());

      System.out.println(map);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 点击推广二维码菜单返回临时图片消息的素材ID
   *
   * @param openId 合伙人对应的微信openId
   * @return mediaId 素材ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String getMediaId(String openId) throws IOException {
    WeiXinOtherUser otherUser = weiXinOtherUserService.findByOpenId(openId);
    if (otherUser != null) {
      WeiXinUser weiXinUser = otherUser.getWeiXinUser();
      if (weiXinUser != null) {
        Partner partner = partnerService.findPartnerByWeiXinUser(weiXinUser).orElse(null);
        if (partner != null) {
          PartnerQrCode partnerQrCode = repository.findByPartner(partner).orElse(null);
          if (partnerQrCode == null) { //创建一个
            Long id = partner.getId();
            //素材已过期，更新素材
            new Thread(() -> sendImg(openId, id, 1)).start();
            return "send:图片合成中,请稍后...";
          } else {
            //先判断二维码是否过期
            if (DateUtils.getTimeStamp() - 2332800
                > partnerQrCode.getDateUpdate().getTime() / 1000) {
              //二维码已过期
              partnerQrCode = createScene(partnerQrCode);
              if (partnerQrCode == null) {
                return "fail create scene";
              }
            }
            //素材未过期，返回
            if (DateUtils.getTimeStamp() - 252000 < partnerQrCode.getMediaCreated()) {
              return partnerQrCode.getMediaId();
            }
            Long id = partnerQrCode.getId();
            //素材已过期，更新素材
            new Thread(() -> sendImg(openId, id, 2)).start();
            return "send:图片合成中,请稍后...";
          }
        }
      }
    }

    return null;
  }

  /**
   * 生成一个合伙人临时二维码海报（包括创建、获取临时二维码、合成图片、生成图片素材）  2017/5/15
   *
   * @param partner 合伙人
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private PartnerQrCode getQrCode(Partner partner) throws IOException {

    PartnerQrCode partnerQrCode = insertQrCode(partner); //创建记录并获取临时二维码
    WeiXinUser weiXinUser = partner.getWeiXinUser();
    if (partnerQrCode != null) {
      return uploadImage(partnerQrCode, weiXinUser.getHeadImageUrl(), weiXinUser.getNickname());
    }
    return null;
  }


  /**
   * 合成合伙人二维码海报  2017/5/12
   */
  private BufferedImage compoundQrCode(String ticket, String headImgUrl, String text)
      throws IOException {
    String sourceFilePath = Constants.WEIXIN_CODE_BACKIMG_URL;
    if (headImgUrl == null) {
      headImgUrl = Constants.WEIXIN_CODE_HEAD_URL;
    }
    if (text == null) {
      text = "乐加生活";
    }
    String
        waterFilePath =
        "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
    // 添加二维码
//    BufferedImage
//        buffImg =
//        ImageUtil
//            .mergeImage(new URL(sourceFilePath), new URL(waterFilePath), new URL(headImgUrl), text,
//                        dictionaryService.findDictionaryById(55L).getValue());
//    String saveFilePath = "I://image//qrcode//" + System.currentTimeMillis() + ".png";
//    ImageUtil.generateWaterFile(buffImg, saveFilePath);

    return ImageUtil
        .mergeImage(new URL(sourceFilePath), new URL(waterFilePath), new URL(headImgUrl), text,
                    dictionaryService.findDictionaryById(55L).getValue());
  }

  /**
   * 创建一个合伙人二维码记录，并创建一个临时二维码  2017/5/12
   *
   * @param partner 合伙人
   * @return 二维码
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private PartnerQrCode insertQrCode(Partner partner) {
    PartnerQrCode qrCode = new PartnerQrCode();
    qrCode.setPartner(partner);
    repository.saveAndFlush(qrCode);
    return createScene(qrCode);
  }

  /**
   * 创建临时二维码  2017/5/15
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private PartnerQrCode createScene(PartnerQrCode qrCode) {
    //获取ID作为场景值ID
    int i = 3;
    while (i > 0) {
      Map<String, Object>
          result =
          weiXinService.getQrCode(qrCode.getId(), Constants.CODE_EXPIRE_SECONDS);
      if (result != null && result.get("ticket") != null) {
        qrCode.setSceneId(qrCode.getId());
        qrCode.setDateUpdate(new Date());
        qrCode.setTicket(String.valueOf(result.get("ticket")));
        qrCode.setUrl(String.valueOf(result.get("url")));
        repository.save(qrCode);
        return qrCode;
      }
      i--;
    }
    return null;
  }


  /**
   * 创建某合伙人临时素材并保存(包括合成图片)  2017/5/12
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private PartnerQrCode uploadImage(PartnerQrCode qrCode, String headImgUrl, String text)
      throws IOException {

    BufferedImage image = compoundQrCode(qrCode.getTicket(), headImgUrl, text);

    Map<String, Object>
        result =
        weiXinService.uploadImage(image, qrCode.getSceneId() + ".png");
    if (result != null && result.get("media_id") != null) {
      qrCode.setMediaCreated(Long.valueOf(result.get("created_at").toString()));
      qrCode.setMediaId(result.get("media_id").toString());
      repository.save(qrCode);
      return qrCode;
    }
    return null;
  }


}
