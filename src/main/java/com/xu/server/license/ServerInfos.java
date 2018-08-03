package com.xu.server.license;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 获取服务器基本信息
 *
 * @author CharleyXu Created on 2018/8/2.
 */
public class ServerInfos extends AbstractServerInfos {

	@Override
	protected String getCPUSerial() throws Exception {
		if (isWindows()) {
			return getWindowsCPUSerial();
		} else {
			return getLinuxCPUSerial();
		}
	}

	@Override
	protected String getMainBoardSerial() throws Exception {
		if (isWindows()) {
			return getWindowsMainBoardSerial();
		} else {
			return getLinuxMainBoardSerial();
		}
	}

	@Override
	protected List<String> getIpAddress() throws Exception {
		List<InetAddress> inetAddress = getInetAddress();
		return inetAddress.stream().map(ip -> ip.getHostAddress())
				.collect(Collectors.toList());
	}

	private String getWindowsCPUSerial() throws Exception {
		//序列号
		String serialNumber = "";
		//使用WMIC获取CPU序列号
		Process process = Runtime.getRuntime().exec("wmic cpu get processorid");
		process.getOutputStream().close();
		Scanner scanner = new Scanner(process.getInputStream());

		if (scanner.hasNext()) {
			scanner.next();
		}
		if (scanner.hasNext()) {
			serialNumber = scanner.next().trim();
		}
		scanner.close();
		return serialNumber;
	}

	private String getLinuxCPUSerial() throws Exception {
		//序列号
		String serialNumber = "";
		//使用dmidecode命令获取CPU序列号
		String[] shell = {"/bin/bash", "-c",
				"dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
		Process process = Runtime.getRuntime().exec(shell);
		process.getOutputStream().close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine().trim();
		if (!Strings.isNullOrEmpty(line)) {
			serialNumber = line;
		}
		reader.close();
		return serialNumber;
	}

	private String getWindowsMainBoardSerial() throws Exception {
		//序列号
		String serialNumber = "";
		//使用WMIC获取主板序列号
		Process process = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
		process.getOutputStream().close();
		Scanner scanner = new Scanner(process.getInputStream());
		if (scanner.hasNext()) {
			scanner.next();
		}
		if (scanner.hasNext()) {
			serialNumber = scanner.next().trim();
		}
		scanner.close();
		return serialNumber;
	}

	private String getLinuxMainBoardSerial() throws Exception {
		//序列号
		String serialNumber = "";
		//使用dmidecode命令获取主板序列号
		String[] shell = {"/bin/bash", "-c",
				"dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
		Process process = Runtime.getRuntime().exec(shell);
		process.getOutputStream().close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = reader.readLine().trim();
		if (!Strings.isNullOrEmpty(line)) {
			serialNumber = line;
		}
		reader.close();
		return serialNumber;
	}

	/**
	 * 验证操作系统类型
	 */
	public boolean isWindows() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			return true;
		}
		return false;
	}
}
