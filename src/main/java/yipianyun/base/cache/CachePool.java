package yipianyun.base.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
 
/**
 * @ClassName: CachePool
 * @Description: TODO(单例Cache池)
 * @author LiuYi
 * @date 2014年6月17日 上午10:50:52
 */
public class CachePool {

	private static  final Log log = LogFactory.getLog(CachePool.class);
	
	private JedisPool pool;
	private static CachePool cachePool;
	
	public static synchronized CachePool getInstance() {
		if (cachePool == null) {
			throw new NullPointerException();
		}
		return cachePool;
	}

	public static synchronized CachePool getInstance(JedisPoolConfig redisConfig, String redisHost, int redisPort,int timeout,String password) {
		if (cachePool == null) {
			cachePool = new CachePool(redisConfig, redisHost, redisPort,timeout,password);
		}
		return cachePool;
	}

	private CachePool(JedisPoolConfig redisConfig, String redisHost, int redisPort) {
		pool = new JedisPool(redisConfig, redisHost, redisPort);
	}
	private CachePool(JedisPoolConfig redisConfig, String redisHost, int redisPort,int timeout,String password) {
		pool = new JedisPool(redisConfig, redisHost, redisPort,timeout,password);
	}
	
	public Jedis getJedis() {
		Jedis jedis = null;
		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
		} catch (JedisConnectionException e) {
			log.error(e);
			borrowOrOprSuccess = false;
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}
		jedis = pool.getResource();
		return jedis;
	}

	public JedisPool getJedisPool() {
		return this.pool;
	}

}