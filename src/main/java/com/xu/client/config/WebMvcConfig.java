package com.xu.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author CharleyXu Created on 2018/8/13.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LicenseInterceptor()).addPathPatterns("/check");
  }
}
