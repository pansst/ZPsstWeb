package com.psst.stock.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.psst.common.log4j.Log4jUtil;
import com.psst.common.service.task.TaskInterface;
import com.psst.common.util.RedisUtil;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.global.GlobalConst;
import com.psst.stock.service.StockServiceI;
import com.psst.stock.util.StockRedisUtil;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 上午10:44:26
 * 任务数据初始化
 */
public class InitInfoTask implements TaskInterface{
	@Autowired
	private StockServiceI stockService;
	
	private static InitInfoTask instance;
	/**
	 * 是否初始化完毕
	 */
	public static boolean initDown = false;
	/**
	 * 保存实例
	 */
	@Override
	public void run() {
		instance = this;
	}
	
	public static InitInfoTask getInstance() {
		return instance;
	}
	public void beginDeal() {
		Jedis  redis = null;
		try {
			Log4jUtil.info("初始化数据.....");
			redis = RedisUtil.getJedis();
			Log4jUtil.info("清除原始stock列表数据.....");
			redis.del(GlobalConst.REDIS_KEY_STORE_STOCK_LIST);
			Log4jUtil.info("清除原始stock列表数据完毕.....");
			Log4jUtil.info("清除原始stock batch数据.....");
			while(redis.llen(GlobalConst.REDIS_KEY_GET_TRANSINFO_THREAD_LIST) > 0) {
				String bacthKey = redis.rpop(GlobalConst.REDIS_KEY_GET_TRANSINFO_THREAD_LIST);
				redis.del(bacthKey);
			}
			Log4jUtil.info("清除原始stock batch数据完毕.....");
			Log4jUtil.info("准备加入新数据.....");
			//查询所有符合条件的stock列表
			List<StockInfo> list = stockService.getAllDealStockInfo();
			//截取
			list = list.subList(0, list.size() > GlobalConst.BATCH_GET_TRANS_INFO_LIST_SIZE ? GlobalConst.BATCH_GET_TRANS_INFO_LIST_SIZE : list.size());
			long indexNumber = 0;
			for(StockInfo stock : list) {
				indexNumber = redis.rpush(GlobalConst.REDIS_KEY_STORE_STOCK_LIST, JSON.toJSONString(stock));
				//保存redis存储信息  以备动态删除指定位置的redis值
				StockRedisUtil.addStockInfoLocation(GlobalConst.REDIS_KEY_STORE_STOCK_LIST, stock.getId(), indexNumber, null);
			}
			Log4jUtil.info("stock列表数据加入缓存，共" + list.size() + "条.....");
			initDown = true;
			//抓取线程启动
			GetStockTransInfoTask.run();
		} catch (Exception e) {
			Log4jUtil.error(e);
		} finally {
			if(null != redis) {
				RedisUtil.returnResource(redis);
			}
		}
	}
}
