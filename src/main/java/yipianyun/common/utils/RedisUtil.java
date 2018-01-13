package yipianyun.common.utils;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import yipianyun.base.cache.Constant;

/** 
 * 单机版redis工具类
 * @author lin.y
 * 2017 下午2:02:24
 */
public class RedisUtil {
	private static final Log log=LogFactory.getLog(RedisUtil.class);

	private  static JedisPool jedisPool;
	
	private static synchronized void initSigle(){
		if(CacheUtils.isInvalidRedisPropertiesValue()){
			String ip=PropertiesUtil.getRedisProperties("redis.host");
			int port=Integer.valueOf(PropertiesUtil.getRedisProperties("redis.port"));
			int timeout=Integer.valueOf(PropertiesUtil.getRedisProperties("redis.timeout"));
			int maxTotal=Integer.valueOf(PropertiesUtil.getRedisProperties("redis.maxTotal"));
			int maxIdle=Integer.valueOf(PropertiesUtil.getRedisProperties("redis.maxIdle"));
			int maxWait=Integer.valueOf(PropertiesUtil.getRedisProperties("redis.maxWait"));
//			String password=PropertiesUtil.getRedisProperties("redis.password");
			try{
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(maxTotal);
					config.setMaxIdle(maxIdle);
					config.setMaxWaitMillis(maxWait);
				
					jedisPool=new JedisPool(config, ip, port, timeout);
				}catch(Exception e){
					log.error("初始化redis-"+CacheUtils.DEFAULT_CACHE_TYPE.toString()+"失败！",e);
				}
			}
		}
	private static Jedis getJedis()throws JedisConnectionException,Exception{
		if(jedisPool==null){
			initSigle();
		}
		Jedis jedis=null;
		try{
			if(jedisPool!=null)
				jedis=jedisPool.getResource();
				return jedis;
		}catch(JedisConnectionException e){
			log.error("Redis-"+CacheUtils.DEFAULT_CACHE_TYPE.toString()+"连接异常",e);
			throw new JedisConnectionException("Redis-"+CacheUtils.DEFAULT_CACHE_TYPE.toString()+"连接异常");
		}catch(Exception e){
			log.error("Get jedis error :Redis-"+CacheUtils.DEFAULT_CACHE_TYPE.name());
			returnResource(jedis);
			throw new Exception("Get jedis error :Redis-"+CacheUtils.DEFAULT_CACHE_TYPE.toString());
		}
	}
	
    /**
     * 释放jedis资源
     * @param jedis
     */
    private static void returnResource(Jedis jedis) {
        if (jedis != null && jedisPool !=null ) {
        	try{
	            jedisPool.returnResource(jedis);
        	}catch(Exception e){
        		log.error("back jedis to RedisPool error！",e);
        	}
        }
    }
    
    /**
     * 设置 String
     * @param key
     * @param value
     */
    public static void setString(String key ,String value){
    	if(value==null)
    		return;
    	Jedis jedis=null;
		try{
			jedis=getJedis();
			jedis.set(key,value);
			jedis.sadd(Constant.IHR_CACHE_PREFIX+CacheUtils.DEFAULT_CACHE_TYPE.name()+"-"+CacheUtils.contentPrefix+"keys(String)", key);
        } catch (Exception e) {
            log.error("Set key error : ",e);
        }finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
    }
     
    /**
     * 设置 过期时间
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public static void setString(String key ,String value,int expire){
    	if(value==null)
    		return;
    	Jedis jedis=null;
		try{
			jedis=getJedis();
			jedis.set(key,value);
			jedis.expire(key, expire);
			jedis.sadd(Constant.IHR_CACHE_PREFIX+CacheUtils.DEFAULT_CACHE_TYPE.name()+"-"+CacheUtils.contentPrefix+"keys(String)", key);
        } catch (Exception e) {
            log.error("Set keyex error : ",e);
        }finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
    }
     
    /**
     * 获取String值
     * @param key
     * @return value
     */
    public static String getString(String key){
    	Jedis jedis=null;
		try{
			jedis=getJedis();
			return jedis.get(key);
    	}catch(Exception e){
    		log.error("Get key error : ",e);
    	}finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
    		return null;
    }
    /**
     * 删除String类型的key-value
     * @param key
     */
    public static void delKeyValue(String key){
    	Jedis jedis=null;
		try{
			jedis=getJedis();
			jedis.del(key);
    	}catch(Exception e){
    		log.error("Delete key-value error : ",e);
    	}finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
    }
    /**
 	 * 新增一个String-key--多个String-value
 	 */
    public static boolean addStringValues(String key,String[] values){
    	if(values==null){
    		return false;
    	}
     	Jedis jedis=null;
    		try{
    			jedis=getJedis();
    			jedis.sadd(key, values);
    			String k=Constant.IHR_CACHE_PREFIX+CacheUtils.DEFAULT_CACHE_TYPE.name()+"-"+CacheUtils.contentPrefix+"keys(String)";
    			log.info("k="+k);
    			jedis.sadd(k, key);
    		return true;
    	}catch(Exception e){
    		log.error("addStringValues error：",e);
    		return false;
    	}finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
    	
    }
    
    /**
 	 * 新增一个String-key--多个String-value
 	 */
    public static void addStringValues(String key,String[] values,int expire){
    	if(values==null){
    		return ;
    	}
    	Jedis jedis=null;
		try{
			jedis=getJedis();
			jedis.sadd(key, values);
			jedis.expire(key, expire);
			jedis.sadd(Constant.IHR_CACHE_PREFIX+CacheUtils.DEFAULT_CACHE_TYPE.name()+"-"+CacheUtils.contentPrefix+"keys(String)", key);
    	}catch(Exception e){
    		log.error("addStringValues error：",e);
    	}finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
    	
    }
    /**
     * 获取一个String-key多个String-value的set集合
     * @param key
     * @return
     */
    public  static Set<String> getStringValues(String key){
    	Set<String> sets=null;
    	Jedis jedis=null;
		try{
			jedis=getJedis();
    			if(jedis==null ||! jedis.exists(key)){
    				return null;
    			}
    			sets=jedis.smembers(key);
    		}catch(Exception e){
    			log.error("getStringValues error：",e);
    		}finally{
            	try {
    				returnResource(jedis);
    			} catch (Exception e) {
    			}
            }
    		return sets;
    }
    
 	/**
 	 * 判断String-key的值sets中某元素是否存在
 	 */
 	public static   boolean sismember(String key,String value){
 		Boolean sismember=null;
		Jedis jedis=null;
			try{
				jedis=getJedis();
			if(jedis==null ||! jedis.exists(key)){
				return false;
			}
				sismember = jedis.sismember(key, value);
		} catch (Exception e) {
			log.error("sismember error：",e);
		}finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
 		return sismember;
 	}
 	
 	/**
 	 * 保存对象
 	 * @param key
 	 * @param object
 	 */
 	public static void saveObject(String key,Object object){
		 		if(object==null){
		 			return;
		 		}
		 		Jedis jedis=null;
 			try{
 				jedis=getJedis();
 				byte[] valueByteArray = SerializeUtil.serializeObject(object);
 				byte[] keyBytes = key.getBytes();
 				jedis.set(keyBytes, valueByteArray);
 				jedis.sadd(Constant.IHR_CACHE_PREFIX+CacheUtils.DEFAULT_CACHE_TYPE.name()+"-"+CacheUtils.contentPrefix+"keys(byte[])", key);
 			}catch(Exception e){
 				log.error("Save cache Object error:",e);
 			}finally{
 	        	try {
 					returnResource(jedis);
 				} catch (Exception e) {
 				}
 	        }
 	}
 	
 	/**
 	 * 保存对象-带指定失效日期
 	 * @param key
 	 * @param object
 	 */
 	public static void saveObject(String key,Object object ,int expire){
		 		if(object==null){
		 			return;
		 		}
		 		Jedis jedis=null;
 			try{
 				jedis=getJedis();
 				byte[] valueByteArray = SerializeUtil.serializeObject(object);
 				byte[] keyBytes = key.getBytes();
 				jedis.set(keyBytes, valueByteArray);
 				jedis.expire(keyBytes, expire);
 				jedis.sadd(Constant.IHR_CACHE_PREFIX+CacheUtils.DEFAULT_CACHE_TYPE.name()+"-"+CacheUtils.contentPrefix+"keys(byte[])", key);
 			}catch(Exception e){
 				log.error("Save cache Object error:",e);
 			}finally{
 	        	try {
 					returnResource(jedis);
 				} catch (Exception e) {
 				}
 	        }
 	}
 	
 	/**
 	 * 获取对象
 	 * @param key
 	 * @param object
 	 */
 	public static Object getObject(String key){
 		Jedis jedis=null;
 			try{
 				jedis=getJedis();
 				byte[] keyBytes = key.getBytes();
 				byte[] bs = jedis.get(keyBytes);
 				Object obj = SerializeUtil.deserializeObject(bs);
 				return obj;
 			}catch(Exception e){
 				log.error("Get cache Object error:",e);
 			}finally{
 	        	try {
 					returnResource(jedis);
 				} catch (Exception e) {
 				}
 	        }
 			return null;
 	}
 	
 	public static void clearCache(String key){
 		Jedis jedis=null;
 		Set<String> keys1=null;
 		Set<String> keys2=null;
		try{
			jedis=getJedis();
			keys1=jedis.smembers(key+"keys(String)");
			keys2=jedis.smembers(key+"keys(byte[])");
			if(keys1!=null){
				for (String ky : keys1) {
					jedis.del(ky);
				}
			}
			if(keys2!=null){
				for (String k : keys2) {
					jedis.del(k.getBytes());
				}
			}
			jedis.del(key+"keys(String)");
			jedis.del(key+"keys(byte[])");
    	}catch(Exception e){
    		log.error("clearCache error : ",e);
    	}finally{
        	try {
				returnResource(jedis);
			} catch (Exception e) {
			}
        }
 	}
}
