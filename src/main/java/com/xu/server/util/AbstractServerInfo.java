package com.xu.server.util;

import com.xu.server.bean.LicenseCheckModel;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CharleyXu Created on 2018/8/2.
 */
public abstract class AbstractServerInfo {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 组装验证信息
	 */
	public LicenseCheckModel getServerInfos() {
		LicenseCheckModel result = new LicenseCheckModel();

		try {
			result.setIpAddress(this.getIpAddress());
			result.setMacAddress(this.getMacAddress());
			result.setCpuSerial(this.getCPUSerial());
			result.setMainBoardSerial(this.getMainBoardSerial());
		} catch (Exception e) {
			logger.error(MessageFormat.format("获取服务器硬件信息失败,{0}", e.getMessage()), e);
		}

		return result;
	}

	/**
	 * 获取CPU序列号
	 */
	protected abstract String getCPUSerial() throws Exception;

	/**
	 * 获取主板序列号
	 */
	protected abstract String getMainBoardSerial() throws Exception;

	/**
	 * 获取本地IP地址
	 */
	protected abstract List<String> getIpAddress() throws Exception;

	/**
	 * 获取本地IP对象
	 */
	protected List<InetAddress> getInetAddress() throws SocketException {
		List<InetAddress> result = new ArrayList<>();
		// 遍历所有的网络接口
		for (Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
				networkInterfaces.hasMoreElements(); ) {
			NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
			// 在所有的接口下再遍历IP
			for (Enumeration inetAddresses = networkInterface.getInetAddresses();
					inetAddresses.hasMoreElements(); ) {
				InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
				//排除LoopbackAddress、LinkLocalAddress、MulticastAddress类型的IP地址
				if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress
						.isMulticastAddress()) {
					continue;
				}
				result.add(inetAddress);
			}
		}
		return result;
	}

	/**
	 * 获取MAC地址的方法
	 */
	protected List<String> getMacAddress() throws Exception {

		List<InetAddress> ias = getInetAddress();
		List<String> resultList = new ArrayList<>(ias.size());
		ias.forEach(ia -> {
			//获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
			byte[] mac = new byte[0];
			try {
				mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
				//把mac地址拼装成String
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < mac.length; i++) {
					if (i != 0) {
						sb.append("-");
					}
					//mac[i] & 0xFF 是为了把byte转化为正整数
					String s = Integer.toHexString(mac[i] & 0xFF);
					sb.append(s.length() == 1 ? 0 + s : s);
				}
				//把字符串所有小写字母改为大写成为正规的mac地址
				resultList.add(sb.toString().toUpperCase());
			} catch (SocketException e) {
				logger.error(MessageFormat.format("获取MAC地址失败,{0}", e.getMessage()), e);
			}
		});
		return resultList;
	}

}
