package com.jifenke.lepluslive.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * http请求工具类 Created by zhangwen on 2016/10/26.
 */
public class HttpUtils {

  public static Map<Object, Object> get(String getUrl) {

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    httpGet.addHeader("Content-Type", "application/json");
    CloseableHttpResponse response = null;
    try {
      response = client.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<Object, Object>
          map =
          mapper.readValue(new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
                           Map.class);
      EntityUtils.consume(entity);
      response.close();
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 可以自定义设置请求头的GET请求，返回结果Map 2017/4/19
   *
   * @param getUrl  请求地址
   * @param headers 请求头
   * @return MAP
   */
  public static Map<String, Object> get(String getUrl, Map<String, String> headers) {

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    headers.forEach((k, v) -> httpGet.addHeader(k, v));

    CloseableHttpResponse response = null;
    try {
      response = client.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object>
          map =
          mapper.readValue(new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
                           Map.class);
      EntityUtils.consume(entity);
      response.close();
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 可以自定义设置请求头的POST请求，返回结果Map 2017/4/19
   *
   * @param postUrl 请求地址
   * @param headers 请求头
   * @param entity  请求实体
   * @return MAP
   */
  public static Map<String, Object> post(String postUrl, Map<String, String> headers,
                                         HttpEntity entity) {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(postUrl);
    headers.forEach((k, v) -> httpPost.addHeader(k, v));
    httpPost.setEntity(entity);

    try {
      CloseableHttpResponse response = httpclient.execute(httpPost);
      entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object>
          map = mapper.readValue(
          new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
          Map.class);
      EntityUtils.consume(entity);
      response.close();
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
   *
   * @param url      请求地址 form表单url地址
   * @param fileName 图片名称
   * @param image    图片缓冲
   * @return String url的响应信息返回值
   */
  public static Map<String, Object> send(String url, String fileName, BufferedImage image)
      throws IOException {

    String result = null;

/**
 * 第一部分
 */
    URL urlObj = new URL(url);
// 连接
    HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

/**
 * 设置关键值
 */
    con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setUseCaches(false); // post方式不能使用缓存

// 设置请求头信息
    con.setRequestProperty("Connection", "Keep-Alive");
    con.setRequestProperty("Charset", "UTF-8");

// 设置边界
    String BOUNDARY = "----------" + System.currentTimeMillis();
    con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

// 请求正文信息

// 第一部分：
    StringBuilder sb = new StringBuilder();
    sb.append("--"); // 必须多两道线
    sb.append(BOUNDARY);
    sb.append("\r\n");
    sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
              + fileName + "\"\r\n");
    sb.append("Content-Type:application/octet-stream\r\n\r\n");

    byte[] head = sb.toString().getBytes("utf-8");

// 获得输出流
    OutputStream out = new DataOutputStream(con.getOutputStream());
// 输出表头
    out.write(head);

// 文件正文部分
// 把文件已流文件的方式 推入到url中
    ImageIO.write(image, "png", out);
// 结尾部分
    byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线

    out.write(foot);

    out.flush();
    out.close();

    StringBuffer buffer = new StringBuffer();
    BufferedReader reader = null;
    try {
// 定义BufferedReader输入流来读取URL的响应
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String line = null;
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }

      result = buffer.toString();

    } catch (IOException e) {
      System.out.println("发送POST请求出现异常！" + e);
      e.printStackTrace();
      throw new IOException("数据读取异常");
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return JsonUtils.jsonToPojo(result, Map.class);
  }


}
