package com.xu.server.controller;

import com.xu.server.bean.LicenseCheckModel;
import com.xu.server.bean.LicenseGeneratorParam;
import com.xu.server.service.LicenseGenerator;
import com.xu.server.util.AbstractServerInfo;
import com.xu.server.util.ServerInfoUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于生成证书文件，不能放在给客户部署的代码里
 *
 * @author CharleyXu Created on 2018/8/3.
 */
@RestController
@RequestMapping("/license")
public class LicenseGeneratorController {

	/**
	 * 证书生成路径
	 */
	@Value("${service.licensePath}")
	private String licensePath;

	/**
	 * 获取服务器硬件信息
	 */
	@RequestMapping(value = "/getServerInfo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public LicenseCheckModel getServerInfo() {
		AbstractServerInfo abstractServerInfo = new ServerInfoUtil();
		return abstractServerInfo.getServerInfos();
	}

	/**
	 * 生成证书
	 */
	@RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public Map<String, Object> generateLicense(
			@RequestBody LicenseGeneratorParam param) {
		Map<String, Object> resultMap = new HashMap<>(3);

		if (StringUtils.isBlank(param.getLicensePath())) {
			param.setLicensePath(licensePath);
		}

		LicenseGenerator licenseCreator = new LicenseGenerator(param);
		boolean result = licenseCreator.generateLicense();

		if (result) {
			resultMap.put("result", "ok");
			resultMap.put("msg", param);
			resultMap.put("code", 1);
		} else {
			resultMap.put("result", "error");
			resultMap.put("msg", "证书文件生成失败！");
			resultMap.put("code", 0);
		}
		return resultMap;
	}

}
