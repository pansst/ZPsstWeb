package com.psst.stock.service;

import java.util.List;

import com.psst.stock.entity.StockInfo;
import com.psst.stock.entity.StockTransInfo;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午1:29:03
 * 类说明
 */
public interface StockServiceI {
	public void addStockInfo(StockInfo stock);
	public void updateStockInfo(StockInfo stock);
	public void updateStockInfoState(int id, int state);
	public List<StockInfo> getAllStockInfo();
	public List<StockInfo> getAllDealStockInfo();
	public StockInfo getStockInfoById(int id);
	public StockInfo getStockInfoByStockCode(String stockCode);
	public StockInfo getStockInfoByStockName(String stockName);
	
	
	public void addStockTransInfo(StockTransInfo stockTransInfo);
}
