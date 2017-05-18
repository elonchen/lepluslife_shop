package com.jifenke.lepluslive.global.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 合成图片
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/12 10:55
 **/
public class ImageUtils {

  /**
   * @param backUrl 背景文件(图片)地址
   * @param alpha   透明度, 选择值从0.0~1.0: 完全透明~完全不透明
   * @return BufferedImage
   * @Title: 构造图片
   * @Description: 生成水印并返回java.awt.image.BufferedImage
   */
  public static BufferedImage watermark(URL backUrl, URL waterFileUrl1, URL waterFileUrl2,
                                        String text, float alpha)
      throws IOException {
    // 获取底图
    BufferedImage buffImg = ImageIO.read(backUrl);

    // 获取层图
    BufferedImage waterImg = ImageIO.read(waterFileUrl1);
    // 创建Graphics2D对象，用在底图对象上绘图
    Graphics2D g2d = buffImg.createGraphics();
//    int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
//    int waterImgHeight = waterImg.getHeight();// 获取层图的高度
    // 在图形和图像中实现混合和透明效果
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
    // 绘制
    g2d.drawImage(waterImg, 230, 295, 290, 290, null);

    // 获取层图
    BufferedImage waterImg2 = ImageIO.read(waterFileUrl2);
    // 切图:　将头像切为圆形
    BufferedImage
        cutHeadImage =
        new BufferedImage(waterImg2.getWidth(), waterImg2.getHeight(),
                          BufferedImage.TYPE_INT_ARGB);
    int minLength = Math.min(cutHeadImage.getWidth(), cutHeadImage.getHeight());
    //设置头像(椭圆形)的坐标和长宽
    Ellipse2D.Double
        shape =
        new Ellipse2D.Double((cutHeadImage.getWidth() - minLength) / 2,
                             (cutHeadImage.getHeight() - minLength) / 2,
                             minLength, minLength);
    Graphics2D g2 = cutHeadImage.createGraphics();
    cutHeadImage =
        g2.getDeviceConfiguration().createCompatibleImage(waterImg2.getWidth(),
                                                          waterImg2.getHeight(),
                                                          Transparency.TRANSLUCENT);
    g2 = cutHeadImage.createGraphics();
    g2.setComposite(AlphaComposite.Clear);
    g2.fill(new Rectangle(cutHeadImage.getWidth(), cutHeadImage.getHeight()));
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
    g2.setClip(shape);
    // 使用 setRenderingHint 设置抗锯齿
    g2.drawImage(waterImg2, 0, 0, null);
    g2.dispose();
    // 开始绘制二维码图片

    // 开始绘制头像图片
    int hdWidth1 = 140, hdHeight1 = 140;
    int
        x2 =
        304;
    int y2 = 1100;
    g2d.drawImage(cutHeadImage, x2, y2, hdWidth1, hdHeight1, null);

    // 开始绘制文字
    int x3 = (buffImg.getWidth() - text.length() * 24) / 2;
    int y3 = 1280;
    g2d.setColor(Color.white);
    g2d.setBackground(Color.white);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setFont(new Font("微软雅黑", Font.BOLD, 26)); //字体、字型、字号

    g2d.drawString(text, x3, y3); //画文字

    g2d.dispose();// 释放图形上下文使用的系统资源
    return buffImg;
  }

  /**
   * 输出水印图片
   *
   * @param buffImg  图像加水印之后的BufferedImage对象
   * @param savePath 图像加水印之后的保存路径
   */
  public static void generateWaterFile(BufferedImage buffImg, String savePath) {
    int temp = savePath.lastIndexOf(".") + 1;
    try {
      ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));

    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

}
