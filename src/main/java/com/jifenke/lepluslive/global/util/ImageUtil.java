package com.jifenke.lepluslive.global.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 图片合成
 *
 * @author zhangwen【zhangwenit@126.com】 2017/6/7 14:22
 **/
public class ImageUtil {

  /**
   * 合成合伙人二维码  2017/6/7
   *
   * @param backImgUrl 背景图片URL
   * @param qrCodeUrl  二维码URL
   * @param headImgUrl 合伙人头像URL
   * @param nickName   合伙人昵称
   * @return 合成后图片
   */
  public static BufferedImage mergeImage(URL backImgUrl, URL qrCodeUrl, URL headImgUrl,
                                         String nickName, String testEncode) throws
                                                                             IOException {

    //获取底图
    BufferedImage backImg = ImageIO.read(backImgUrl);
    BufferedImage qrCodeImg = ImageIO.read(qrCodeUrl);
    BufferedImage headImg = ImageIO.read(headImgUrl);
    //创建绘图对象
    Graphics2D g = backImg.createGraphics();
    // 在图形和图像中实现混合和透明效果
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
    //绘制二维码
    g.drawImage(qrCodeImg, 240, 482, 270, 270, null);
    //将头像切成圆形
    BufferedImage
        cutHeadImg =
        new BufferedImage(headImg.getWidth(), headImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
    int minLength = Math.min(cutHeadImg.getWidth(), cutHeadImg.getHeight());
    Ellipse2D.Double
        shape =
        new Ellipse2D.Double((cutHeadImg.getWidth() - minLength) / 2,
                             (cutHeadImg.getHeight() - minLength) / 2, minLength, minLength);
    Graphics2D g2 = cutHeadImg.createGraphics();
    cutHeadImg =
        g2.getDeviceConfiguration().createCompatibleImage(headImg.getWidth(), headImg.getHeight(),
                                                          Transparency.TRANSLUCENT);
    g2 = cutHeadImg.createGraphics();
    g2.setComposite(AlphaComposite.Clear);
    g2.fill(new Rectangle(cutHeadImg.getWidth(), cutHeadImg.getHeight()));
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
    g2.setClip(shape);
    g2.drawImage(headImg, 0, 0, null);
    g2.dispose();
    //绘制头像
    int headWidth = 136, headHeight = 136;
    int headX = 308, headY = 182;
    g.drawImage(cutHeadImg, headX, headY, headWidth, headHeight, null);
    //绘制昵称
    if ("1".equals(testEncode)) {
      nickName = new String(nickName.getBytes("utf8"), "iso8859-1");
    }
    int nameX = (backImg.getWidth() - nickName.length() * 24) / 2;
    int nameY = 394;
    g.setColor(Color.black);
    g.setBackground(Color.black);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setFont(new Font("微软雅黑", Font.BOLD, 26));
    g.drawString(nickName, nameX, nameY);

    g.dispose();
    return backImg;
  }

//  /**
//   * 输出水印图片
//   *
//   * @param buffImg  图像加水印之后的BufferedImage对象
//   * @param savePath 图像加水印之后的保存路径
//   */
//  public static void generateWaterFile(BufferedImage buffImg, String savePath) {
//    int temp = savePath.lastIndexOf(".") + 1;
//    try {
//      ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
//
//    } catch (IOException e1) {
//      e1.printStackTrace();
//    }
//  }


}
