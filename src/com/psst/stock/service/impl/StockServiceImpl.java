package com.psst.stock.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psst.common.dao.BaseDaoI;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.entity.StockTransInfo;
import com.psst.stock.service.StockServiceI;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午1:29:54
 * 类说明
 */
@Service
public class StockServiceImpl implements StockServiceI {

	@Autowired
	private BaseDaoI<StockInfo> stockInfoDao;
	
	@Autowired
	private BaseDaoI<StockTransInfo> stockTransInfoDao;
	
	public void updateStockInfo(StockInfo stock) {
		stockInfoDao.update(stock);
	}
	
	@Override
	public void updateStockInfoState(int id, int state) {
		StockInfo stock = getStockInfoById(id);
		if(null != stock) {
			stock.setState(state);
			stock.setUpdateTime(new Date());
			stockInfoDao.update(stock);
		}
	}
	@Override
	public void addStockTransInfo(StockTransInfo stockTransInfo) {
		if(null != stockTransInfoDao)
			stockTransInfoDao.save(stockTransInfo);
	}
	@Override
	public void addStockInfo(StockInfo stock) {
		StockInfo oldStock = getStockInfoByStockCode(stock.getStockCode()); 
		if(null == oldStock) {
			stockInfoDao.save(stock);
		} else {
			//TODO暂不处理
		}
	}

	@Override
	public List<StockInfo> getAllStockInfo() {
		return stockInfoDao.find("from StockInfo");
	}

	public List<StockInfo> getAllDealStockInfo() {
		return stockInfoDao.find("from StockInfo where state = 1 or state = 2");
	}
	@Override
	public StockInfo getStockInfoById(int id) {
		return stockInfoDao.get(StockInfo.class, id);
	}

	@Override
	public StockInfo getStockInfoByStockCode(String stockCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stockCode", stockCode);
		return stockInfoDao.get("from StockInfo si where si.stockCode=:stockCode", params);
	}

	@Override
	public StockInfo getStockInfoByStockName(String stockName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stockName", stockName);
		return stockInfoDao.get("from StockInfo si where si.stockName=:stockName", params);
	}
}
