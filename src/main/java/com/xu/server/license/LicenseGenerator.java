package com.xu.server.license;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseCreator;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

/**
 * License生成类
 *
 * @author CharleyXu Created on 2018/8/3.
 */
public class LicenseGenerator {

	private static Logger logger = LogManager.getLogger(LicenseCreator.class);
	private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal(
			"CN=localhost, OU=localhost, O=localhost, L=BJ, ST=BJ, C=CN");
	private LicenseGeneratorParam param;

	public LicenseGenerator(LicenseGeneratorParam param) {
		this.param = param;
	}

	/**
	 * 生成License证书
	 */
	public boolean generateLicense() {
		try {
			logger.info(param);
			LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
			LicenseContent licenseContent = initLicenseContent();

			licenseManager.store(licenseContent, new File(param.getLicensePath()));
			return true;
		} catch (Exception e) {
			logger.error(MessageFormat.format("证书生成失败：{0}", e.getMessage()), e);
			return false;
		}
	}

	/**
	 * 初始化证书生成参数
	 */
	private LicenseParam initLicenseParam() {
		Preferences preferences = Preferences.userNodeForPackage(LicenseGenerator.class);
		//设置对证书内容加密的秘钥
		CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

		KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseGenerator.class
				, param.getPrivateKeysStorePath()
				, param.getPrivateAlias()
				, param.getStorePass()
				, param.getKeyPass());

		LicenseParam licenseParam = new DefaultLicenseParam(param.getSubject()
				, preferences
				, privateStoreParam
				, cipherParam);

		return licenseParam;
	}

	/**
	 * 设置证书生成正文信息
	 */
	private LicenseContent initLicenseContent() {
		LicenseContent licenseContent = new LicenseContent();
		licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
		licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

		licenseContent.setSubject(param.getSubject());
		licenseContent.setIssued(param.getIssuedTime());
		licenseContent.setNotBefore(param.getIssuedTime());
		licenseContent.setNotAfter(param.getExpireTime());
		licenseContent.setConsumerType(param.getConsumerType());
		licenseContent.setConsumerAmount(param.getConsumerAmount());
		licenseContent.setInfo(param.getDescription());

		//扩展校验服务器硬件信息
		licenseContent.setExtra(param.getLicenseCheckModel());

		return licenseContent;
	}

}
