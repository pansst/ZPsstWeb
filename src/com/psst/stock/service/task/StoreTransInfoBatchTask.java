package com.psst.stock.service.task;

import org.hibernate.Session;
import org.hibernate.Transaction;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.psst.common.log4j.Log4jUtil;
import com.psst.common.service.TaskSessionFactory;
import com.psst.common.service.task.TaskInterface;
import com.psst.common.util.RedisUtil;
import com.psst.stock.entity.StockTransInfo;
import com.psst.stock.global.GlobalConst;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 下午3:32:31
 * 类说明
 */
public class StoreTransInfoBatchTask implements TaskInterface{
	private String key;
	private int index;
	public StoreTransInfoBatchTask(String key, int index) {
		super();
		this.key = key;
		this.index = index;
	}

	public boolean haveDataToStore() {
		Jedis  redis = null;
		try {
			redis = RedisUtil.getJedis();
			return redis.llen(key) > 0;
		} catch (Exception e) {
			Log4jUtil.error(e);
		}  finally {
			if(null != redis) {
				RedisUtil.returnResource(redis);
			}
		}
		return true;
	}
	@Override
	public void run() {
		Log4jUtil.info("线程[" + index + "]开始抓存储信息......");
		while(true) {
			if (!GlobalConst.canGetTransInfo() && !haveDataToStore()) {
				Log4jUtil.info("线程[" + index + "]结束存储信息......");
				return;
			}
			Jedis  redis = null;
			Session session = null;
			try {
				redis = RedisUtil.getJedis();
				int size = 0;
				if(redis.llen(key) > 0) {
					Log4jUtil.info("线程[" + index + "]保存线上数据，开始.....");
					session = TaskSessionFactory.getSessionFactory().openSession();
					Transaction tx = session.beginTransaction();
					while(redis.llen(key) > 0 && size < GlobalConst.BTRANS_INFO_STOR_BATCH_SIZE) {
						String info = redis.rpop(key);
						StockTransInfo stockTransInfo = JSON.parseObject(info, StockTransInfo.class);
						session.save(stockTransInfo);
						//stockService.addStockTransInfo(stockTransInfo);
						size ++;
					}
					tx.commit();
					Log4jUtil.info("线程[" + index + "]保存线上数据，结束，处理[" + size +"]条数据.....");
				} else {
					try {
						Thread.sleep(GlobalConst.BACTH_SLEEP_TIME);
					} catch (Exception e) {
						Log4jUtil.error(e);
					}
				}
			} catch (Exception e) {
				Log4jUtil.error(e);
			} finally {
				if(null != redis) {
					RedisUtil.returnResource(redis);
				}
				if(null != session) {
					session.close();
				}
			}
		}
	}

}
