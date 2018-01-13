package yipianyun.common.utils;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @describe:读取properties系统配置文件
 * 
 * @author ylin  2017年3月9日 下午3:06:17
 */
@SuppressWarnings("unused")
public class PropertiesUtil {
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
	private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");
    private static final Pattern PORT_PATTERN=Pattern.compile("^\\d{1,5}$");
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
	private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	private static final Log log=LogFactory.getLog(PropertiesUtil.class);
	/**
	 * 
	 * @param filename为文件名不包括扩展名，若包含就出错了噢，该方法适用于多语言环境中
	 * @param key
	 * @returnString
	 */
	public static String getProperties(String filename,String key){
		String value = "";
		if (StringUtils.isEmpty(filename)) {
			filename = "system";
		}
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(filename);
			value = bundle.getString(key);
			if(StringUtils.isNotEmpty(value)){
					value=new String(value.getBytes("ISO-8859-1"),"utf-8");
			}
		} catch (Exception e) {
			log.error("解析"+filename+"文件错误",e);
		}
		return value;
	}
	
	public static String getRedisProperties(String key){
		String value = "";
	
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("redis");
			value = bundle.getString(key);
			if(StringUtils.isNotEmpty(value)){
					value=new String(value.getBytes("ISO-8859-1"),"utf-8");
			}
		} catch (Exception e) {
			log.error("解析redis配置文件错误",e);
		}
		return value;
	}
	
	public static boolean isInvalidIp(String ip){
		return IP_PATTERN.matcher(ip).matches();
	}
	public static boolean isInvalidPort(String port){
		return PORT_PATTERN.matcher(port).matches() 
				&& Integer.valueOf(port)<=MAX_PORT 
				&& Integer.valueOf(port)>MIN_PORT;
	}
	public static boolean isInvalidInt(String a){
		return INT_PATTERN.matcher(a).matches();
	}
}
