package com.xu.client.service;

import com.xu.client.bean.LicenseVerifyParam;
import com.xu.server.bean.CustomKeyStoreParam;
import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CharleyXu Created on 2018/8/13.
 *
 * License校验类
 */
public class LicenseVerify {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 安装证书
   */
  public synchronized LicenseContent install(LicenseVerifyParam licenseVerifyParam) {
    LicenseContent result = null;
    try {
      LicenseManager licenseManager = LicenseManagerHolder
          .getLicenseManager(initLicenseParam(licenseVerifyParam));
      licenseManager.uninstall();
      result = licenseManager.install(new File(licenseVerifyParam.getLicensePath()));
      logger.info(
          MessageFormat
              .format("证书安装成功，证书有效期：{0} - {1}", result.getNotBefore(), result.getNotAfter()));
    } catch (Exception e) {
      logger.error("证书安装失败" + e.getMessage(), e);
    }
    return result;

  }

  public boolean verify() {
    LicenseManager licenseManager = LicenseManagerHolder
        .getLicenseManager(null);
    try {
      LicenseContent licenseContent = licenseManager.verify();
      logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}", licenseContent.getNotBefore(),
          licenseContent.getNotAfter()));
      return true;
    } catch (Exception e) {
      logger.error("证书校验失败！", e);
      return false;
    }
  }

  /**
   * 初始化License
   */
  private LicenseParam initLicenseParam(LicenseVerifyParam param) {
    Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

    CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

    KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
        , param.getPublicKeysStorePath()
        , param.getPublicAlias()
        , param.getStorePass()
        , null);

    return new DefaultLicenseParam(param.getSubject()
        , preferences
        , publicStoreParam
        , cipherParam);
  }

}
