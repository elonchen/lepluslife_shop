package com.jifenke.lepluslive.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * APP签名
 *
 * @author zhangwen【zhangwenit@126.com】 2017/6/8 16:21
 **/
@Configuration
public class SignUtil {

  //<editor-fold desc="服务器钥匙对">
  /**
   * 客户端公钥
   */
  private static String pub;

/*
  @Value("#{T(org.apache.commons.io.FileUtils).readFileToString(" +
         "T(org.springframework.util.ResourceUtils).getFile('classpath:key/sign-pub-key.txt')" +
         ")}")
  public void setPub(String key) {
    pub = key;
  }
*/

  /**
   * 服务器私钥
   */
  private static String pri;

/*
  @Value("#{T(org.apache.commons.io.FileUtils).readFileToString(" +
         "T(org.springframework.util.ResourceUtils).getFile('classpath:key/sign-pri-key.txt')" +
         ")}")
  public void setPri(String key) {
    pri = key;
  }
*/


  private static final String ALGORITHM = "RSA";
  private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
  private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
  private static final String CHARSET = "UTF-8";


  /**
   * 公钥加密-私钥解密：加密
   *
   * @param content 原字符串
   * @return 加密后字符串
   */
  public static String encodePublic(String content) {
    try {
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(pub));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] contentBytes = content.getBytes(CHARSET);
      int inputLen = contentBytes.length;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int offSet = 0;
      byte[] cache;
      int i = 0;
      // 对数据分段加密
      while (inputLen - offSet > 0) {
        if (inputLen - offSet > 117) {
          cache = cipher.doFinal(contentBytes, offSet, 117);
        } else {
          cache = cipher.doFinal(contentBytes, offSet, inputLen - offSet);
        }
        out.write(cache, 0, cache.length);
        i++;
        offSet = i * 117;
      }
      byte[] encryptedData = out.toByteArray();
      out.close();
      return Base64.encode(encryptedData);
    } catch (Exception e) {
    }
    return null;
  }

  /**
   * 公钥加密-私钥解密：解密
   *
   * @param ciphertext 被加密字符串
   * @return 已解密字符串
   */
  public static String decodePublic(String ciphertext) {
    try {
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(pri));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] encryptedData = Base64.decode(ciphertext);
      int inputLen2 = encryptedData.length;
      ByteArrayOutputStream out2 = new ByteArrayOutputStream();
      int offSet2 = 0;
      byte[] cache2;
      int i2 = 0;
      // 对数据分段解密
      while (inputLen2 - offSet2 > 0) {
        if (inputLen2 - offSet2 > 128) {
          cache2 = cipher.doFinal(encryptedData, offSet2, 128);
        } else {
          cache2 = cipher.doFinal(encryptedData, offSet2, inputLen2 - offSet2);
        }
        out2.write(cache2, 0, cache2.length);
        i2++;
        offSet2 = i2 * 128;
      }
      byte[] decryptedData = out2.toByteArray();
      out2.close();
      return new String(decryptedData);
    } catch (Exception e) {
    }
    return null;
  }

  /**
   * 私钥加密-公钥解密：加密
   *
   * @param content 原字符串
   * @return 加密后字符串
   */
  public static String encodePrivate(String content) {
    try {
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(pri));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);
      byte[] contentBytes = content.getBytes(CHARSET);
      int inputLen = contentBytes.length;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int offSet = 0;
      byte[] cache;
      int i = 0;
      // 对数据分段加密
      while (inputLen - offSet > 0) {
        if (inputLen - offSet > 117) {
          cache = cipher.doFinal(contentBytes, offSet, 117);
        } else {
          cache = cipher.doFinal(contentBytes, offSet, inputLen - offSet);
        }
        out.write(cache, 0, cache.length);
        i++;
        offSet = i * 117;
      }
      byte[] encryptedData = out.toByteArray();
      out.close();
      return Base64.encode(encryptedData);
    } catch (Exception e) {
    }
    return null;
  }

  /**
   * 私钥加密-公钥解密：解密
   *
   * @param ciphertext 被加密字符串
   * @return 已解密字符串
   */
  public static String decodePrivate(String ciphertext) {
    try {
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(pub));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, publicKey);
      byte[] encryptedData = Base64.decode(ciphertext);
      int inputLen2 = encryptedData.length;
      ByteArrayOutputStream out2 = new ByteArrayOutputStream();
      int offSet2 = 0;
      byte[] cache2;
      int i2 = 0;
      // 对数据分段解密
      while (inputLen2 - offSet2 > 0) {
        if (inputLen2 - offSet2 > 128) {
          cache2 = cipher.doFinal(encryptedData, offSet2, 128);
        } else {
          cache2 = cipher.doFinal(encryptedData, offSet2, inputLen2 - offSet2);
        }
        out2.write(cache2, 0, cache2.length);
        i2++;
        offSet2 = i2 * 128;
      }
      byte[] decryptedData = out2.toByteArray();
      out2.close();
      return new String(decryptedData);
    } catch (Exception e) {
    }
    return null;
  }

  /**
   * 私钥签名
   *
   * @param str 原字符串
   * @return 被签名字符串
   */
  public static String sign(String str) {
    try {
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(pri));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
      Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
      signature.initSign(privateKey);
      signature.update(str.getBytes(CHARSET));
      byte[] result = signature.sign();
      return Base64.encode(result);
    } catch (Exception e) {
    }
    return null;
  }

  /**
   * SHA1WithRSA 验证签名
   *
   * @param str     原字符串
   * @param signStr 签名后字符串
   * @return 验证结果
   */
  public static boolean testSign(String str, String signStr) {
    try {
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(pub));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
      Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
      signature.initVerify(publicKey);
      signature.update(str.getBytes(CHARSET));
      byte[] encryptedData = Base64.decode(signStr);
      return signature.verify(encryptedData);
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * RSA 公钥、私钥的生成
   */
  public static void initKeys() {
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyPairGen.initialize(1024);
      KeyPair keyPair = keyPairGen.generateKeyPair();
      PublicKey publicKey = keyPair.getPublic();
      PrivateKey privateKey = keyPair.getPrivate();

      String publicKeyStr = getKey(publicKey);
      System.out.println(publicKeyStr);
      String privateKeyStr = getKey(privateKey);
      System.out.println(privateKeyStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Hex方式: MHex.encodeHexString(keyData);
   * Base64方式：(new BASE64Encoder()).encodeBuffer(keyData);
   */
  public static String getKey(Key key) {
    byte[] keyData = key.getEncoded();
    return Base64.encode(keyData);
  }

}
