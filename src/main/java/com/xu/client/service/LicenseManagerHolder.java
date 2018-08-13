package com.xu.client.service;

import com.xu.server.service.CustomLicenseManager;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @author CharleyXu Created on 2018/8/13.
 */
public class LicenseManagerHolder {

  private static volatile CustomLicenseManager licenseManager;

  private LicenseManagerHolder() {

  }

  public static LicenseManager getLicenseManager(LicenseParam licenseParam) {
    if (licenseManager == null) {
      synchronized (LicenseManagerHolder.class) {
        if (licenseManager == null) {
          licenseManager = new CustomLicenseManager(licenseParam);
        }
      }
    }
    return licenseManager;
  }

}
