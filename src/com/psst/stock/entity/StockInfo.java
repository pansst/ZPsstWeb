package com.psst.stock.entity;

import java.util.Date;

import com.psst.common.entity.BaseEntity;

/**
 * 
* @author yongsheng.shi
* @version 创建时间：2017-3-22 上午11:48:28
* 类说明
 */
public class StockInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int type = 0;
	private String typeStr;
	private String stockCode;
	private String stockName;
	private String attentionState = "0";
	private int buyState = 0;
	private double realPrice = 0;
	private double supplyStocks = 0;
	private double stockWorth = 0;
	private int state = 0;
	private Date updateTime;
	
	
	public StockInfo() {
		super();
	}

	public StockInfo(int id, String typeStr, String stockCode) {
		super();
		this.id = id;
		this.typeStr = typeStr;
		this.stockCode = stockCode;
	}

	public void setAttr(StockInfo stockInfo) {
		if(null != stockInfo) {
			this.type = stockInfo.type;
			this.stockCode = stockInfo.stockCode;
			this.stockName = stockInfo.stockName;
			this.attentionState = stockInfo.attentionState;
			this.buyState = stockInfo.buyState;
			this.realPrice = stockInfo.realPrice;
			this.supplyStocks = stockInfo.supplyStocks;
			this.stockWorth = stockInfo.stockWorth;
			this.state = stockInfo.state;
			this.updateTime = stockInfo.updateTime;
			this.typeStr = stockInfo.typeStr;
		}
		
	}
	
	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	public String getAttentionState() {
		return attentionState;
	}
	public void setAttentionState(String attentionState) {
		this.attentionState = attentionState;
	}
	public int getBuyState() {
		return buyState;
	}
	public void setBuyState(int buyState) {
		this.buyState = buyState;
	}
	public double getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}
	public double getSupplyStocks() {
		return supplyStocks;
	}
	public void setSupplyStocks(double supplyStocks) {
		this.supplyStocks = supplyStocks;
	}
	public double getStockWorth() {
		return stockWorth;
	}
	public void setStockWorth(double stockWorth) {
		this.stockWorth = stockWorth;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
