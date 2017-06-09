package com.jifenke.lepluslive.global.filter;

import com.jifenke.lepluslive.global.config.AppConstants;
import com.jifenke.lepluslive.global.util.SignUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验签拦截器
 *
 * @author zhangwen【zhangwenit@126.com】 2017/6/9 16:21
 **/
public class SignFilter implements HandlerInterceptor {

  private static Logger logger = LoggerFactory.getLogger(SignFilter.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse httpServletResponse, Object o) throws Exception {
    Map<String, String[]> params = request.getParameterMap();
    TreeMap<String, Object> parameters = new TreeMap<>();
    for (String key : params.keySet()) {
      String[] values = params.get(key);
      for (String val : values) {
        parameters.put(key, val);
      }
    }
    System.out.println("请求数据==================" + parameters.toString());

    //时间戳验证
    long currTime = System.currentTimeMillis();
    long reqTime = Long.valueOf(parameters.get("timestamp").toString());
    if (currTime + AppConstants.REQUEST_TIMESTAMP_ALLOW_RANGE < reqTime ||
        reqTime < currTime - AppConstants.REQUEST_TIMESTAMP_ALLOW_RANGE) {
      logger.error("时间戳有误" + reqTime);
      return false;
    }

    String sign = String.valueOf(parameters.get("sign"));
    parameters.remove("sign");

    StringBuilder sb = new StringBuilder();
    parameters.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
    String requestStr = sb.deleteCharAt(sb.length() - 1).toString();

    if (SignUtil.testSign(requestStr, sign)) { //验签
      System.out.println("success");
      return true;
    }
    logger.error("签名有误" + requestStr);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse, Object o,
                         ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse, Object o, Exception e)
      throws Exception {

  }
}
