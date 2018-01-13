package com.aliyun.api.getway.constant;
/**
 * @desc 	    ：短信配置	 
 * @author  ：	 一片云
 * @version ： 2017年9月29日  上午10:40:20
 */
public class SMSConf {
	/**
	 * 账号注册功能
	 */
	public static  final String accountRegist="SMS_001";
	/**
	 * 密码找回功能
	 */
	public static  final String passwdFind="SMS_002"; 
	/**
	 * 验证码默认有效时间10分钟
	 */
	public static  final short default_expire=10;
	/**
	 *验证码默认发送次数3次/天
	 */
	public static  final short num_day=3;
}
