package com.jifenke.lepluslive.web.rest;


import com.jifenke.lepluslive.Application;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.HttpUtils;
import com.jifenke.lepluslive.partner.service.PartnerQrCodeService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;


/**
 * Created by wcg on 16/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles({Constants.SPRING_PROFILE_DEVELOPMENT})
public class QrCode {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private PartnerQrCodeService partnerQrCodeService;


  //微信获取临时二维码测试
  @Test
  public void getQrCodeTest() {

    Map<String, Object> map = weiXinService.getQrCode(12345L, Constants.CODE_EXPIRE_SECONDS);

    System.out.println(map);

  }

  //合成图片测试
  @Test
  public void twoPictureTest() throws IOException {

    partnerQrCodeService.compoundQrCode("gQEp8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyZTBMYXBkM0NmVjExZDQ5WXhwMXEAAgREPBVZAwQAjScA");

  }

  //上传临时素材测试
  @Test
  public void uploadTest() throws IOException, URISyntaxException {

    BufferedImage
        image =
        partnerQrCodeService.compoundQrCode(
            "gQEp8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyZTBMYXBkM0NmVjExZDQ5WXhwMXEAAgREPBVZAwQAjScA");

    String
        fileUrl =
        "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    String fileName = "4343434.png";

    String
        url =
        "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=1OP7-OSdShRTjcidzAUno1cimYzGJB0gBbVHXbEQlnEiyq_0C2r6jkATvwq5v6voG3qui4txPCbyZ1M39v0IP2NmE-WQ3LvWgdR2v3rMrC8mV9EoYern417dB6j0DyVfSATcACABJC&type=image";

    String result = HttpUtils.send(url, fileName, image);

    System.out.println(result);


  }

  //时间戳测试
  @Test
  public void timeTest() throws IOException, URISyntaxException {

    Long times = new Date().getTime() / 1000;
    System.out.println(times);

    System.out.println(times - 1494571374);

  }
}

////  public static void main(String[] args) {
////    int x[][] = new int[9][9];
////    for(int i=0;i<9;i++){
////      for(int y=0;y<9;y++){
////        x[i][y]=new Random().nextInt(2);
////      }
////    }
////    Scanner input = new Scanner(System.in);
////    int a = input.nextInt();
////    int b = input.nextInt();
////    int n = input.nextInt();
////
////    for(int z=1;z<n;z++){
////      int m = x[a][b];
////      int a1 = x[a-1][b];
////      int a2 = x[a+1][b];
////      int a3 = x[a][b+1];
////      int a4 = x[a][b-1];
////
////
////
////    }
//
//
//
//  }


