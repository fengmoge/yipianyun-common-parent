package yipianyun.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {

	public static String min(String[] arr){
		long dt= Long.valueOf(arr[0].replaceAll("-", ""));
		for (String date : arr) {
			if (StringUtils.isBlank(date)) continue;
			long da= Long.valueOf(date.replaceAll("-", ""));
			if (da<dt){
				dt=da;
			}
		}
		String dateStr = String.valueOf(dt);
		if (dateStr.length()>=8){
			return dateStr.substring(0,4)+"-"+dateStr.substring(4,6)+"-"+dateStr.substring(6,8);
		}
		return dateStr;
	}
	
	public static String max(String[] arr){
		long dt=0L;
		for (String date : arr) {
			if (StringUtils.isBlank(date)) continue;
			long da= Long.valueOf(date.replaceAll("-", ""));
			if (da>dt){
				dt=da;
			}
		}
		String dateStr = String.valueOf(dt);
		if (dateStr.length()>=8){
			return dateStr.substring(0,4)+"-"+dateStr.substring(4,6)+"-"+dateStr.substring(6,8);
		}
		return dateStr;
	}
	
	public static int between(String date1,String date2,String date){
		if (StringUtils.isBlank(date)){
			throw new IllegalArgumentException("参数有误，参数日期格式为yyyy-MM-dd");
		}
		date1=date1.replaceAll("-", "");
		date2=date2.replaceAll("-", "");
		date=date.replaceAll("-", "");
		if (date.length()!=date1.length() || date.length()!=date2.length() || date1.length()!=date2.length()){
			throw new IllegalArgumentException("参数有误，参数日期格式为yyyy-MM-dd");
		}
		long dt1=Long.valueOf(date1);
		long dt2=Long.valueOf(date2);
		long dt=Long.valueOf(date);
		
		if (dt1<dt2){
			if (dt1<=dt && dt<=dt2){
				return 0;
			} else if (dt<dt1){
				return -1;
			} else {
				return 1;
			}
		} else if (dt1> dt2){
			if (dt1>=dt && dt>=dt2){
				return 0;
			} else if (dt<dt2){
				return -1;
			} else {
				return 1;
			}
		} else {
			if (dt==dt1){
				return 0;
			} else if (dt<dt1){
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public static  String beforeDay(String date) throws ParseException {
		Calendar ca = Calendar.getInstance();
		Date da=null;
		da=new SimpleDateFormat("yyyy-MM-dd").parse(date);
		ca.setTime(da);
		ca.add(Calendar.DATE, -1);
		String beforeDay = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
		return beforeDay;
	}
	public static  String beforeMonth(String date) throws ParseException {
		Calendar ca = Calendar.getInstance();
		Date da=null;
		da=new SimpleDateFormat("yyyy-MM-dd").parse(date);
		ca.setTime(da);
		ca.add(Calendar.MONTH, -1);
		String beforeDay = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
		return beforeDay;
	}
	
	public static long strDate2Time(String date) throws ParseException {
		if (StringUtils.isEmpty(date)) return 0L;
		Date da;
		if (date.length()==10) {
			 da = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} else if (date.length()==19) {
			da = new SimpleDateFormat("yyyy-MM-dd HH mm ss").parse(date);
		} else {
			return 0L;
		}
		long time = da.getTime();
		return time;
	}
}
