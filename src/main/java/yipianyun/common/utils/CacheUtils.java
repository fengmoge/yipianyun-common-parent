package yipianyun.common.utils;

 
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import yipianyun.base.cache.CacheType;
import yipianyun.base.cache.Constant;
import yipianyun.common.utils.PropertiesUtil;
/**
 * 高可用可扩展缓存工具类
 * @author lin.y
 * 2017 下午2:01:46
 */
@SuppressWarnings("unused")
public class CacheUtils {
    
	private static final Log log=LogFactory.getLog(CacheUtils.class);
	
	private  static JedisPool jedisPool;
	
	private static final  int DEFAULT_EXRP=Constant.EXRP_HOUR;
	public  static  final CacheType DEFAULT_CACHE_TYPE=CacheType.NONE;
	public static String contentPrefix=Constant.EJOB_PREFIX;
	
		
		public static boolean   isInvalidRedisPropertiesValue(){
			boolean check=true;
			String ip=PropertiesUtil.getRedisProperties("redis.host");
			String port=PropertiesUtil.getRedisProperties("redis.port");
			String timeout=PropertiesUtil.getRedisProperties("redis.timeout");
			String maxTotal=PropertiesUtil.getRedisProperties("redis.maxTotal");
			String maxIdle=PropertiesUtil.getRedisProperties("redis.maxIdle");
			String maxWait=PropertiesUtil.getRedisProperties("redis.maxWait");
			if(!PropertiesUtil.isInvalidIp(ip)){
				log.error("Redis配置文件非法IP格式:"+ip);
				check=false;
			}
			if(!PropertiesUtil.isInvalidPort(port)){
				log.error("Redis配置文件非法端口格式:"+port);
				check=false;
			}
			if(!PropertiesUtil.isInvalidInt(timeout) 
					|| !PropertiesUtil.isInvalidInt(maxTotal)
					|| !PropertiesUtil.isInvalidInt(maxIdle)
					|| !PropertiesUtil.isInvalidInt(maxWait)){
			check=false;
			log.error("Redis配置文件非法参数格式");
			}
			return check;
		}

		
		
	    
	    /**
	     * 设置 String
	     * @param key
	     * @param value
	     */
	    public static void setString(String key ,String value){
	        try {
	           switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return;
					case SIGLE:
						 RedisUtil.setString(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,value);
					case CLUSTER:
						return;
					case MEMORY:
						return;
					default:
						return;
	           }
	        } catch (Exception e) {
	            log.error("Set cache key error : ",e);
	        }
	    }
	     
	    /**
	     * 设置 过期时间
	     * @param key
	     * @param seconds 以秒为单位
	     * @param value
	     */
	    public static void setString(String key ,String value,int seconds){
	        try {
	            switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return;
					case SIGLE:
						 RedisUtil.setString(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,value,seconds);
					case CLUSTER:
						return;
					case MEMORY:
						return;
					default:
						return;
	           }
	        } catch (Exception e) {
	            log.error("Set cache keyex error : ",e);
	        }
	    }
	     
	    /**
	     * 获取String值
	     * @param key
	     * @return value
	     */
	    public static String getString(String key){
	    	try{
	    		  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return null;
					case SIGLE:
						return RedisUtil.getString(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key);
					case CLUSTER:
						return null;
					case MEMORY:
						return null;
					default:
						return null;
	           }
	    	}catch(Exception e){
	    		log.error("Get cache key error : ",e);
	    	}
	    	return null;
	    }
	    
	    /**
	     * 删除String类型的key-value
	     * @param key
	     */
	    public static void delKey(String key){
	    	try{
	    		  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return ;
					case SIGLE:
						RedisUtil.delKeyValue(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key);
					case CLUSTER:
						return ;
					case MEMORY:
						return ;
					default:
						return ;
	           }
	    	}catch(Exception e){
	    		log.error("Del cachec Key error : ",e);
	    	}
	    }
	    
	    /**
	 	 * 新增一个String-key--多个String-value
	 	 */
	    public static boolean addValues(String key,String[] values){
	    	try{
	    		  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return false;
					case SIGLE:
						return RedisUtil.addStringValues(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,values);
					case CLUSTER:
						return false;
					case MEMORY:
						return false;
					default:
						return false;
	           }
	    	}catch(Exception e){
	    		log.error("Add cache values error : ",e);
	    	}
	    				return false;
	    }
	    
	    /**
	 	 * 新增一个String-key--多个String-value,带指定失效时间
	 	 */
	    public static void addValues(String key,String[] values,int expire){
	    	try{
	    		  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return ;
					case SIGLE:
						RedisUtil.addStringValues(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,values,expire);
					case CLUSTER:
						return ;
					case MEMORY:
						return ;
					default:
						return ;
	           }
	    	}catch(Exception e){
	    		log.error("Add cache values error : ",e);
	    	}
	    }
	    
	    /**
	     * 获取一个String-key多个String-value的set集合
	     * @param key
	     * @return
	     */
	    public  static Set<String> getStringValues(String key){
	    	try{
	    		  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return null;
					case SIGLE:
						return RedisUtil.getStringValues(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key);
					case CLUSTER:
						return null;
					case MEMORY:
						return null;
					default:
						return null;
	           }
	    	}catch(Exception e){
	    		log.error("Get cache StringValues error : ",e);
	    	}
	    	return null;
	    }
	    
	 	/**
	 	 * 判断String-key的值sets中某元素是否存在
	 	 */
	 	public static   boolean sismember(String key,String value){
	 		try{
	    		  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return false;
					case SIGLE:
						return RedisUtil.sismember(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,value);
					case CLUSTER:
						return false;
					case MEMORY:
						return false;
					default:
						return false;
	           }
	    	}catch(Exception e){
	    		log.error(" Sismember cache value from values of key error : ",e);
	    	}
	 		return false;
	 	}
	 	
	 	/**
	 	 * 保存对象
	 	 * @param key
	 	 * @param obj
	 	 */
	 	public static void saveObject(String key,Object obj){
	 		try{
	 			  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return ;
					case SIGLE:
						RedisUtil.saveObject(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,obj);
					case CLUSTER:
						return ;
					case MEMORY:
						return ;
					default:
						return ;
	           }
	 		}catch(Exception e){
	 			log.error(" Save cache object error:",e);
	 		}
	 	}
	 	
	 	/**
	 	 * 保存对象
	 	 * @param key
	 	 * @param obj
	 	 */
	 	public static void saveObject(String key,Object obj,int expire){
	 		try{
	 			  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return ;
					case SIGLE:
						RedisUtil.saveObject(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key,obj,expire);
					case CLUSTER:
						return ;
					case MEMORY:
						return ;
					default:
						return ;
	           }
	 		}catch(Exception e){
	 			log.error(" Save cache object error:",e);
	 		}
	 	}
	 	/**
	 	 * 获取对象
	 	 * @param key
	 	 * @param obj
	 	 */
	 	public static Object getObject(String key){
	 		try{
	 			  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return null;
					case SIGLE:
						return RedisUtil.getObject(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+key);
					case CLUSTER:
						return null;
					case MEMORY:
						return null;
					default:
						return null;
	           }
	 		}catch(Exception e){
	 			log.error(" Get cache object error:",e);
	 		}
	 					return null;
	 	}
	 	
	 	/**
	 	 * 清空当前缓存下指定项目的缓存
	 	 * 形参contentPrefix 表示项目前缀，例如Contant.EJOB_PREFIX=EJOB-
	 	 * @param contentPrefix
	 	 */
	 	public static void clearCache(final String contentPrefix){
	 		try{
	 			  switch(DEFAULT_CACHE_TYPE){
		           case NONE:
						return ;
					case SIGLE:
						RedisUtil.clearCache(Constant.IHR_CACHE_PREFIX+DEFAULT_CACHE_TYPE.name()+"-"+contentPrefix);
					case CLUSTER:
						return ;
					case MEMORY:
						 MemoryUtil.clearCache();
					default:
						return ;
	           }
	 		}catch(Exception e){
	 			log.error(" Clear cache- "+DEFAULT_CACHE_TYPE.toString()+"  error:",e);
	 		}
	 	}
	 	
	 	/**
	 	 * 保存Collection
	 	 * @param key
	 	 * @param Collection,CacheType=CacheType.MEMORY
	 	 */
	 	public static void putCollection(String key,Collection<?> collection){
	 		try{
						 MemoryUtil.setCollection(Constant.IHR_CACHE_PREFIX+CacheType.MEMORY.name()+"-"+key, collection);
	 		}catch(Exception e){
	 			log.error(" Save Collection cache  error:",e);
	 		}
	 	}	
	 	/**
	 	 * 保存Collection
	 	 * @param key
	 	 * @param Collection,CacheType=CacheType.MEMORY
	 	 */
	 	public static Collection<?> getCollection(String key){
	 		try{
						return  MemoryUtil.getCollection(Constant.IHR_CACHE_PREFIX+CacheType.MEMORY.name()+"-"+key);
	 		}catch(Exception e){
	 			log.error(" Save Collection cache  error:",e);
	 		}
	 		return null;
	 	}	
}
