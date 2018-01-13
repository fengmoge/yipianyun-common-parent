package yipianyun.common.utils;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;


public class UrlUtil {
	private static final String  HTTP_PROTOCOL="http";
	private static final String  HTTPS_PROTOCOL="https";
	public static String getRequestIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)){
			 //多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index!=-1){
				return ip.substring(0,index);
			} else {
				return ip;
			}
		}
		ip=request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)){
			return ip;
		}

		return request.getRemoteAddr();
	}
	
	public static String getHost(String urlStr) throws Exception{
		URL url = new URL(urlStr);
		return url.getHost();
	}
	public static String getPort(String urlStr) throws Exception{
		URL url = new URL(urlStr);
		return  String.valueOf(url.getPort());
	}
	public static String getPath(String urlStr) throws Exception{
		URL url = new URL(urlStr);
		return url.getPath();
	}
	public static String getProtocol(String urlStr) throws Exception{
		URL url = new URL(urlStr);
		return url.getProtocol();
	}
	public static String getUri(String urlStr) throws Exception{
		StringBuilder sb = new StringBuilder();
		if (urlStr.startsWith(HTTP_PROTOCOL) || urlStr.startsWith(HTTPS_PROTOCOL)){
			URL url = new URL(urlStr);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String port= String.valueOf(url.getPort());
			if ("-1".equals(port)) port="";
			sb.append(protocol).append("://").append(host).append(port);
		} else {
			throw new IllegalArgumentException(urlStr+":地址异常");
		}
		return sb.toString();
	}
}
