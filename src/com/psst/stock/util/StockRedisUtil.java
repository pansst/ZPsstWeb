package com.psst.stock.util;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.util.RedisUtil;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-27 下午5:03:02
 * stock操作redis工具
 */
public class StockRedisUtil {
	/**
	 * stock信息redis信息索引index缓存
	 */
	private static Map<String,Map<Integer,Long>> stockLocationMaps = new HashMap<String, Map<Integer,Long>>();
	
	
	public static Map<String, Map<Integer, Long>> getStockLocationMaps() {
		return stockLocationMaps;
	}
	/**
	 * 添加记录信息  自然序编号
	 * @param redisKey
	 * @param id
	 * @param indexNumber
	 * @param data
	 */
	public static void addStockInfoLocation(String redisKey, Integer id, Long indexNumber, Object data) {
		addStockInfoLocation(redisKey, id, indexNumber - 1);
	}
	/**
	 * 添加记录信息  数组编号
	 * @param redisKey
	 * @param id
	 * @param indexArray
	 */
	public static void addStockInfoLocation(String redisKey, Integer id, Long indexArray) {
		Map<Integer,Long> redisMaps = stockLocationMaps.get(redisKey);
		if(null == redisMaps) {
			redisMaps = new HashMap<Integer, Long>();
			stockLocationMaps.put(redisKey, redisMaps);
		}
		redisMaps.put(id, indexArray);
	}
	/**
	 * 从指定key种移除指定id的值
	 * @param redisKey
	 * @param id
	 */
	public static void removeStockInfoFromListById(String redisKey, Integer id) {
		Jedis redis = null;
		try {
			redis = RedisUtil.getJedis();
			long len = redis.llen(redisKey);
			if(stockLocationMaps.containsKey(redisKey) && stockLocationMaps.get(redisKey) != null && stockLocationMaps.get(redisKey).get(id) != null && stockLocationMaps.get(redisKey).get(id) <= len) {
				redis = RedisUtil.getJedis();
				long storeIndex = stockLocationMaps.get(redisKey).get(id);
				for(long index = 0; index < storeIndex; index ++) {
					redis.rpush(redisKey, redis.lpop(redisKey));
				}
				//移除数据
				redis.lpop(redisKey);
			} else {
				Log4jUtil.info("移除stock信息不存在[" + redisKey + "]:[" + id + "]");
			}
			
		} catch (Exception e) {
			Log4jUtil.error("移除stock信息出错[" + redisKey + "]:[" + id + "]" , e);
		} finally {
			if(null != redis) {
				RedisUtil.returnResource(redis);
			}
		}
	}
}
