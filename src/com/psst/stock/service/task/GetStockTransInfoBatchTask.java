package com.psst.stock.service.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.psst.common.log4j.Log4jUtil;
import com.psst.common.service.task.TaskInterface;
import com.psst.common.util.DateUtil;
import com.psst.common.util.RedisUtil;
import com.psst.common.util.StockUtil;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.entity.StockTransInfo;
import com.psst.stock.global.GlobalConst;
import com.psst.stock.util.StockRedisUtil;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 上午10:57:04
 * 批量抓取信息 任务实体
 */
public class GetStockTransInfoBatchTask implements TaskInterface{
	/**
	 * 任务信息存储key
	 */
	private String bacthRedisKey;
	/**
	 * 抓取信息存储key
	 */
	private String transInfoKey;
	/**
	 * 任务编号
	 */
    private int index;
    /**
     * 时间格式化类  非线程安全 每个任务独享
     */
    private SimpleDateFormat sdf;
	public GetStockTransInfoBatchTask(int index, String bacthRedisKey, String transInfoKey) {
		super();
		this.bacthRedisKey = bacthRedisKey;
		this.transInfoKey = transInfoKey;
		this.index = index;
		this.sdf = DateUtil.getBaseDateFormat();
	}
	
	@Override
	public void run() {
		Log4jUtil.info("线程[" + index + "]开始抓取线上信息......");
		while (true) {
			if (!GlobalConst.canGetTransInfo()) {
				Log4jUtil.info("线程[" + index + "]结束抓取线上信息......");
				return;
			}
			Jedis  redis = null;
			try {
				redis = RedisUtil.getJedis();
				long len = redis.llen(bacthRedisKey);
				Log4jUtil.info("线程[" + index + "]抓取线上信息，预计[" + len + "]条......");
				List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
				for (int index = 0; index < len; index ++) {
					String info = redis.lindex(bacthRedisKey, index);
					StockInfo stock = JSON.parseObject(info, StockInfo.class);
					stockInfoList.add(stock);
				}
				List<StockTransInfo> transInfoList = StockUtil.getTransInfo(stockInfoList, sdf);
				for (StockTransInfo transInfo : transInfoList) {
					if (transInfo.getId() == -1) {
						//错误数据  已删除
						redis.rpush(GlobalConst.REDIS_KEY_BAD_STOCK_LIST, transInfo.getStockId() + "");
						//移除错误数据
						StockRedisUtil.removeStockInfoFromListById(bacthRedisKey, transInfo.getStockId());
						//移除原始列表
						StockRedisUtil.removeStockInfoFromListById(GlobalConst.REDIS_KEY_STORE_STOCK_LIST, transInfo.getStockId());
					} else {
						String info = JSON.toJSONString(transInfo);
						redis.rpush(GlobalConst.REDIS_KEY_STOCK_TRANSINFO_LIST, info);
						redis.rpush(transInfoKey, info);
					}
				}
				Log4jUtil.info("线程[" + index + "]抓取线上信息完毕，获取[" + transInfoList.size() + "]条.....");
			} catch (Exception e) {
				Log4jUtil.error("线程[" + index + "]抓取线上信息失败", e);
			} finally {
				if (null != redis) {
					RedisUtil.returnResource(redis);
				}
			}
			try {
				Thread.sleep(GlobalConst.BACTH_SLEEP_TIME);
			} catch (Exception e) {
				Log4jUtil.error(e);
			}
		}
	}
}
