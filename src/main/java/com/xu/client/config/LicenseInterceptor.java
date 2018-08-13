package com.xu.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xu.client.service.LicenseVerify;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author CharleyXu Created on 2018/8/13.
 *
 * 拦截器校验
 */
public class LicenseInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    LicenseVerify licenseVerify = new LicenseVerify();

    //校验证书是否有效
    boolean verifyResult = licenseVerify.verify();

    if (verifyResult) {
      return true;
    } else {
      response.setCharacterEncoding("UTF-8");
      Map<String, String> result = new HashMap<>(1);
      result.put("result", "Invalid Certificate！");
      response.getWriter().write(new ObjectMapper().writeValueAsString(result));
      return false;
    }
  }
}
