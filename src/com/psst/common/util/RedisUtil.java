package com.psst.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.psst.common.log4j.Log4jUtil;

public class RedisUtil {
//	private static Logger log = Logger.getLogger(RedisUtil.class);
//	private static String ADDR = new PropertiesLoader("system.properties").getProperty("redis_ip");
//	private static int PORT = new PropertiesLoader("system.properties").getInteger("redis_port"); 
	private static int MAX_ACTIVE = 1024;
	private static int MAX_IDLE = 30;
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	private static boolean TEST_ON_BORROW = true;
//	private static JedisPool jedisPool = null;
	private static final String BASE_REDIS_CONF_TYPE = "base";
	private static Map<Jedis, JedisPool> poolResourceMaps;
	private static Map<String, JedisPool> poolMaps;
	static {
		try {
			poolResourceMaps = new HashMap<Jedis, JedisPool>();
			poolMaps = new HashMap<String, JedisPool>();
			PropertiesLoader propertiesLoader = new PropertiesLoader("system.properties");
			String redisConfig = propertiesLoader.getProperty("redis_config");
			String[] cons = redisConfig.trim().split(",");
			for (String redisConfType : cons) {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(MAX_ACTIVE);
				config.setMaxIdle(MAX_IDLE);
				config.setMaxWaitMillis(MAX_WAIT);
				config.setTestOnBorrow(TEST_ON_BORROW);
				String addr = propertiesLoader.getProperty("redis_" + redisConfType + "_ip");
				int port = propertiesLoader.getInteger("redis_" + redisConfType + "_port");
				JedisPool jedisPool = new JedisPool(config, addr, port, TIMEOUT);
				poolMaps.put(redisConfType, jedisPool);
			}
		} catch (Exception e) {
			Log4jUtil.error(e);
		}
	}

	public synchronized static Jedis getJedis() {
		return getJedis(BASE_REDIS_CONF_TYPE);
	}
	
	public synchronized static Jedis getJedis(String confType) {
		try {
			if (!StringUtils.isEmpty(confType) && null != poolMaps && poolMaps.get(confType) != null) {
				Jedis resource = poolMaps.get(confType).getResource();
				if (null != resource) {
					poolResourceMaps.put(resource, poolMaps.get(confType));
				}
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			Log4jUtil.error(e);
			return null;
		}
	}
	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null && poolResourceMaps.containsKey(jedis) && poolResourceMaps.get(jedis) != null) {
			JedisPool jedisPool = poolResourceMaps.remove(jedis);
			jedisPool.returnResource(jedis);
			//jedisPool.returnResource(jedis);
		}
	}
}
