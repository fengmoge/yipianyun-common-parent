package yipianyun.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
	public static final int DEFAULT_LENGTH=8;
	public static String  generateNum(){
		return RandomStringUtils.randomNumeric(DEFAULT_LENGTH);
	}
	public static String  generateNum(int length){
		return RandomStringUtils.randomNumeric(length);
	}
}
