package com.psst.stock.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.psst.common.log4j.Log4jUtil;
import com.psst.common.service.task.TaskInterface;
import com.psst.common.util.RedisUtil;
import com.psst.stock.entity.StockTransInfo;
import com.psst.stock.service.StockServiceI;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 上午11:35:03
 * 线上信息保存
 */
@Service
public class StoreTransInfoTask implements TaskInterface{
	@Autowired
	private StockServiceI stockService;
	/**
	 * 保存全局实例
	 */
	private static StoreTransInfoTask instance;
	
	@Override
	public void run() {
		instance = this;
	}
	/*public static StoreTransInfoTask getInstance() {
		return instance;
	}*/
	public static void storeTransInfo(String redisKey) {
		if (!StringUtils.isEmpty(redisKey)) {
			instance.addTransInfoRun(redisKey);
		}
	}
	
	private void addTransInfoRun(String redisKey) {
		Jedis  redis = null;
		try {
			Log4jUtil.info("保存线上数据[" + redisKey + "]，开始.....");
			redis = RedisUtil.getJedis();
			int size = 0;
			//全部取完
			while (redis.llen(redisKey) > 0) {
				String info = redis.rpop(redisKey);
				StockTransInfo stockTransInfo = JSON.parseObject(info, StockTransInfo.class);
				stockService.addStockTransInfo(stockTransInfo);
				size ++;
			}
			Log4jUtil.info("保存线上数据结束[" + redisKey + "]，处理[" + size +"]条数据.....");
		} catch (Exception e) {
			Log4jUtil.error(e);
		} finally {
			if (null != redis) {
				RedisUtil.returnResource(redis);
			}
		}
	}
}
