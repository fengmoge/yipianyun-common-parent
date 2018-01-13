package yipianyun.common.utils;
 
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import yipianyun.common.core.SysConstants;

public class FieldAccessUtil {

	private static final Log log = LogFactory.getLog(FieldAccessUtil.class);

	private static boolean flag;

	/**
	 * 为单个对象
	 * 添加、修改
	 * 审计字段赋值 
	 * 
	 * @param obj
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static boolean setFieldValue(Object obj, String str)
			throws Exception {
		try {
			Class<?> clz = obj.getClass();
			List<String> list = new ArrayList<String>();
			Field[] fields = clz.getDeclaredFields();
			for(int i = 0; i < fields.length; i++) {
				list.add(fields[i].getName());
			}
			if(list.contains(SysConstants.CREATOR) || list.contains(SysConstants.MODIFIER)) {
				String userid = null;
				if (null != userid) {
					if (SysConstants.INSERT.equals(str)) {
						clz.getDeclaredMethod(capitalizeFirstLetter(SysConstants.CREATOR), String.class).invoke(obj, userid);
					} else {
						clz.getDeclaredMethod(capitalizeFirstLetter(SysConstants.MODIFIER), String.class).invoke(obj, userid);
					}
				} else {
					if (SysConstants.INSERT.equals(str)) {
						clz.getDeclaredMethod(capitalizeFirstLetter(SysConstants.CREATOR), String.class).invoke(obj, "moren");
					} else {
						clz.getDeclaredMethod(capitalizeFirstLetter(SysConstants.MODIFIER), String.class).invoke(obj, "moren");
					}
				}
			}
			if (list.contains(SysConstants.CREATETIME) || list.contains(SysConstants.MODIFYTIME)) {
				if (SysConstants.INSERT.equals(str)) {
					clz.getDeclaredMethod(capitalizeFirstLetter(SysConstants.CREATETIME), Long.class).invoke(obj, System.currentTimeMillis());
				} else {
					log.info(SysConstants.MODIFYTIME);
					clz.getDeclaredMethod(capitalizeFirstLetter(SysConstants.MODIFYTIME), Long.class).invoke(obj, System.currentTimeMillis());
				}
			}
			flag = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag = false;
		}
		return flag;
	}

	/**
	 * 批量添加，批量赋值 拦截参数为List
	 * 
	 * @param object
	 * @param insert
	 * @throws Exception
	 */
	public static void setFieldValueList(List<?> list, String str)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			// list存的类型为map
			if (obj instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) obj;
				setFieldValueMap(map, str);
			} else {
				// 存放为对象
				setFieldValue(obj, str);
			}

		}
	}

	/**
	 * 拦截参数为Map
	 * 
	 * @param object
	 * @param insert
	 */
	@SuppressWarnings("unchecked")
	public static void setFieldValueMap(Map map, String mark) {
		String userid = null;
		if (SysConstants.INSERT.equals(mark)) {
			if (null != userid) {
				map.put(SysConstants.CREATOR, userid);
			} else {
				map.put(SysConstants.CREATOR, "moren");
			}
			map.put(SysConstants.CREATETIME, System.currentTimeMillis());
		} else if (SysConstants.UPDATE.equals(mark)) {
			if (null != userid) {
				map.put(SysConstants.MODIFIER, userid);
			} else {
				map.put(SysConstants.MODIFIER, "moren");
			}
			map.put(SysConstants.MODIFYTIME, System.currentTimeMillis());
		}
	}
	
	private static String capitalizeFirstLetter(String str){
		
		return "set" + str.substring(0,1).toUpperCase() + str.substring(1);
	}
}
