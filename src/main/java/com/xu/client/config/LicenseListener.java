package com.xu.client.config;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 监听器
 *
 * @author CharleyXu Created on 2018/8/13.
 */
public class LicenseListener implements ApplicationListener<ContextRefreshedEvent> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  /**
   * 证书subject
   */
  @Value("${license.subject}")
  private String subject;

  /**
   * 公钥别称
   */
  @Value("${license.publicAlias}")
  private String publicAlias;

  /**
   * 访问公钥库的密码
   */
  @Value("${license.storePass}")
  private String storePass;

  /**
   * 证书生成路径
   */
  @Value("${license.licensePath}")
  private String licensePath;

  /**
   * 密钥库存储路径
   */
  @Value("${license.publicKeysStorePath}")
  private String publicKeysStorePath;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    ApplicationContext context = contextRefreshedEvent.getApplicationContext().getParent();
    if (context == null) {
      if (!Strings.isNullOrEmpty(licensePath)) {
        logger.info("=====开始安装证书 =====");
//        LicenseVerifyParam param = new LicenseVerifyParam();
//        param.setSubject(subject);
//        param.setPublicAlias(publicAlias);
//        param.setStorePass(storePass);
//        param.setLicensePath(licensePath);
//        param.setPublicKeysStorePath(publicKeysStorePath);
//        LicenseVerify licenseVerify = new LicenseVerify();
//        //安装证书
//        licenseVerify.install(param);
        logger.info("====== 证书安装结束 =====");
      }
    }

  }
}
