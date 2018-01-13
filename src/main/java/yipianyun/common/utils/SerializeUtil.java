package yipianyun.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
/**
 * 序列化工具类
 * @author lin.y
 * 2017 下午2:04:47
 */
public class SerializeUtil {
	private static final Log log=LogFactory.getLog(SerializeUtil.class);
	
	/**
	 * 将java的对象转换成二进制数据
	 * @param obj
	 * @return
	 */
	public static  byte[] serializeObject(Object obj){
		ByteArrayOutputStream byteOut =null;
		ObjectOutputStream objectOut = null;
		try{
			byteOut = new ByteArrayOutputStream();
			objectOut = new ObjectOutputStream(byteOut);
			objectOut.writeObject(obj);
			byte[] byteArray = byteOut.toByteArray();
			return byteArray;
		}catch(Exception e){
			log.error("序列化对象失败："+obj.getClass().getName());
		}finally{
				if(byteOut!=null){
					try {
						byteOut.close();
					} catch (IOException e) {
					}
				if(objectOut!=null){
					try {
						objectOut.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 反序列化-将二进制数据转换成对象
	 * @param obj
	 * @return
	 */
	public static Object deserializeObject(byte[] byteArray){
		if(byteArray==null)
			return null;
		ByteArrayInputStream byteInput =null;
		ObjectInputStream objectInput = null;
		try{
			byteInput=new ByteArrayInputStream(byteArray);
			objectInput=new ObjectInputStream(byteInput);
			Object obj = objectInput.readObject();
			return obj;
		}catch(Exception e){
			log.error("反序列化对象失败",e);
		}finally{
				if(byteInput!=null){
					try {
						byteInput.close();
					} catch (IOException e) {
					}
				if(objectInput!=null){
					try {
						objectInput.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return null;
	}
}
