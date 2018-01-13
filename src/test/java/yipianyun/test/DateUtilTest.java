package yipianyun.test;

import java.text.ParseException;

import yipianyun.common.utils.DateUtil;

public class DateUtilTest {

	public static void main(String[] args) throws ParseException {
//		long time = DateUtil.strDate2Time("2017-12-21 00 00 00");
		long time = DateUtil.strDate2Time("2017-12-21");
		System.out.println(time);

	}

}
