package com.psst.stock.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.util.RedisUtil;
import com.psst.stock.global.GlobalConst;
import com.psst.stock.service.StockServiceI;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 下午1:46:01
 * 错误数据更新
 */
@Service
public class BadStockDealTask {
	@Autowired
	private StockServiceI stockService;
	public void updateBadStock() {
		Jedis  redis = null;
		try {
			Log4jUtil.info("处理垃圾数据，开始.....");
			redis = RedisUtil.getJedis();
			int size = 0;
			while (redis.llen(GlobalConst.REDIS_KEY_BAD_STOCK_LIST) > 0) {
				String info = redis.rpop(GlobalConst.REDIS_KEY_BAD_STOCK_LIST);
				stockService.updateStockInfoState(Integer.parseInt(info), GlobalConst.STOCK_STATE_DELETE);
				size ++;
			}
			Log4jUtil.info("处理垃圾数据，结束，处理[" + size + "]条数据.....");
		} catch (Exception e) {
			Log4jUtil.error(e);
		} finally {
			if (null != redis) {
				RedisUtil.returnResource(redis);
			}
		}
	}
}
