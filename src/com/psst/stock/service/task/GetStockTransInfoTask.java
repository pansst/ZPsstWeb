package com.psst.stock.service.task;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.psst.common.log4j.Log4jUtil;
import com.psst.common.util.RedisUtil;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.global.GlobalConst;
import com.psst.stock.util.StockRedisUtil;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午8:33:08
 * 抓取线上信息 任务
 */
@Service
public class GetStockTransInfoTask {
	
	/**
	 * 抓取线上信息任务分配
	 */
	public static void run() {
		if (!GlobalConst.canGetTransInfo()) {
			return;
		}
		Jedis  redis = null;
		try {
			Log4jUtil.info("数据抓取分配.....");
			redis = RedisUtil.getJedis();
			long len = redis.llen(GlobalConst.REDIS_KEY_STORE_STOCK_LIST);
			int size = 0;
			//分批任务批次号
			int batchIndex = 0;
			//分批 任务redis key
			String bacthKeyStr = GlobalConst.REDIS_KEY_GET_STOCK_TRANSINFO_BACTH_LIST + batchIndex;
			long indexNumber = 0;
			for(int index = 0; index < len;index ++) {
				String info = redis.lindex(GlobalConst.REDIS_KEY_STORE_STOCK_LIST, index);
				//任务批次计算
				if(size % GlobalConst.BATCH_GET_TRANS_INFO_SIZE == 0) {
					batchIndex ++;
					bacthKeyStr = GlobalConst.REDIS_KEY_GET_STOCK_TRANSINFO_BACTH_LIST + batchIndex;
					//记录任务匹配key  以被后续清理
					redis.rpush(GlobalConst.REDIS_KEY_GET_TRANSINFO_THREAD_LIST, bacthKeyStr);
				}
				size ++;
				//将任务数据加入指定key
				indexNumber = redis.rpush(bacthKeyStr, info);
				StockInfo stock = JSON.parseObject(info, StockInfo.class);
				//保存redis存储信息  以备动态删除指定位置的redis值
				StockRedisUtil.addStockInfoLocation(bacthKeyStr, stock.getId(), indexNumber, null);
			}
			Log4jUtil.info("数据抓取分配完毕，共" + len + "条数据，线程数： " + (batchIndex + 1) + ".....");
			//开始启动线程
			for(int index = 1; index < batchIndex; index ++) {
				/**
				 * 多线程抓取
				 */
				GetStockTransInfoBatchTask task = new GetStockTransInfoBatchTask(index, GlobalConst.REDIS_KEY_GET_STOCK_TRANSINFO_BACTH_LIST + index, GlobalConst.REDIS_KEY_STORE_STOCK_TRANSINFO_LIST + index);
				new Thread(task).start();
				/**
				 * 多线程存储
				 */
				StoreTransInfoBatchTask storeTask = new StoreTransInfoBatchTask(GlobalConst.REDIS_KEY_STORE_STOCK_TRANSINFO_LIST + index, index);
				new Thread(storeTask).start();
				Log4jUtil.info("数据抓取线程,存取线程[" + index + "]启动完毕....");
			}
		} catch (Exception e) {
			Log4jUtil.error("数据抓取分配出错", e);
		} finally {
			if(null != redis) {
				RedisUtil.returnResource(redis);
			}
		}
	}
	
	
	
}
