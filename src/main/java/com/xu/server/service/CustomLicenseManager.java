package com.xu.server.service;

import com.xu.server.bean.LicenseCheckModel;
import com.xu.server.util.ServerInfoUtil;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义LicenseManager，用于增加额外的服务器硬件信息校验
 *
 * @author CharleyXu Created on 2018/8/3.
 */
public class CustomLicenseManager extends LicenseManager {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //XML编码
  private static final String XML_CHARSET = "UTF-8";
  //默认BUFSIZE
  private static final int DEFAULT_BUF_SIZE = 8 * 1024;

  public CustomLicenseManager() {

  }

  public CustomLicenseManager(LicenseParam param) {
    super(param);
  }

  /**
   * 重写create方法
   */
  @Override
  protected synchronized byte[] create(
      LicenseContent content,
      LicenseNotary notary)
      throws Exception {
    initialize(content);
    this.validateCreate(content);
    final GenericCertificate certificate = notary.sign(content);
    return getPrivacyGuard().cert2key(certificate);
  }

  /**
   * 重写install方法
   */
  @Override
  protected synchronized LicenseContent install(
      final byte[] key,
      final LicenseNotary notary)
      throws Exception {
    final GenericCertificate certificate = getPrivacyGuard().key2cert(key);

    notary.verify(certificate);
    final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
    this.validate(content);
    setLicenseKey(key);
    setCertificate(certificate);

    return content;
  }

  /**
   * 重写verify方法
   */
  @Override
  protected synchronized LicenseContent verify(final LicenseNotary notary)
      throws Exception {
    // Load service key from preferences,
    final byte[] key = getLicenseKey();
    if (null == key) {
      throw new NoLicenseInstalledException(getLicenseParam().getSubject());
    }

    GenericCertificate certificate = getPrivacyGuard().key2cert(key);
    notary.verify(certificate);
    final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
    this.validate(content);
    setCertificate(certificate);

    return content;
  }

  /**
   * 校验生成证书的参数信息
   */
  protected synchronized void validateCreate(final LicenseContent content)
      throws LicenseContentException {

    final Date now = new Date();
    final Date notBefore = content.getNotBefore();
    final Date notAfter = content.getNotAfter();
    if (null != notAfter && now.after(notAfter)) {
      throw new LicenseContentException("证书失效时间不能早于当前时间");
    }
    if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
      throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
    }
    final String consumerType = content.getConsumerType();
    if (null == consumerType) {
      throw new LicenseContentException("用户类型不能为空");
    }
  }


  /**
   * 重写validate方法，增加IP地址、Mac地址等其他信息校验
   */
  @Override
  protected synchronized void validate(final LicenseContent content)
      throws LicenseContentException {
    //1. 首先调用父类的validate方法
    super.validate(content);

    //2. 然后校验自定义的License参数
    //License中可被允许的参数信息
    LicenseCheckModel expectedCheckModel = (LicenseCheckModel) content
        .getExtra();
    //当前服务器真实的参数信息
    LicenseCheckModel serverCheckModel = getServerInfo();

    if (expectedCheckModel != null && serverCheckModel != null) {
      //校验IP地址
      if (!checkIpAddress(expectedCheckModel.getIpAddress(), serverCheckModel.getIpAddress())) {
        throw new LicenseContentException("当前服务器的IP没在授权范围内");
      }

      //校验Mac地址
      if (!checkIpAddress(expectedCheckModel.getMacAddress(), serverCheckModel.getMacAddress())) {
        throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
      }

      //校验主板序列号
      if (!checkSerial(expectedCheckModel.getMainBoardSerial(),
          serverCheckModel.getMainBoardSerial())) {
        throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
      }

      //校验CPU序列号
      if (!checkSerial(expectedCheckModel.getCpuSerial(), serverCheckModel.getCpuSerial())) {
        throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
      }
    } else {
      throw new LicenseContentException("不能获取服务器硬件信息");
    }
  }


  /**
   * 重写XMLDecoder解析XML
   */
  private Object load(String encoded) {
    BufferedInputStream inputStream = null;
    XMLDecoder decoder = null;
    try {
      inputStream = new BufferedInputStream(
          new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));

      decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUF_SIZE), null, null);

      return decoder.readObject();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } finally {
      try {
        if (decoder != null) {
          decoder.close();
        }
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (Exception e) {
        logger.error("XMLDecoder解析XML失败", e);
      }
    }

    return null;
  }

  /**
   * 获取当前服务器需要额外校验的License参数
   */
  private LicenseCheckModel getServerInfo() {
    return new ServerInfoUtil().getServerInfos();
  }

  /**
   * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内<br/> 如果存在IP在可被允许的IP/Mac地址范围内，则返回true
   */
  private boolean checkIpAddress(List<String> expectedList, List<String> serverList) {
    if (expectedList != null && expectedList.size() > 0) {
      if (serverList != null && serverList.size() > 0) {
        for (String expected : expectedList) {
          if (serverList.contains(expected.trim())) {
            return true;
          }
        }
      }
      return false;
    } else {
      return true;
    }
  }

  /**
   * 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
   */
  private boolean checkSerial(String expectedSerial, String serverSerial) {
    if (StringUtils.isNotBlank(expectedSerial)) {
      if (StringUtils.isNotBlank(serverSerial)) {
        if (expectedSerial.equals(serverSerial)) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
  }

}
