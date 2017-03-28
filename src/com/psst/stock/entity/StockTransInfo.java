package com.psst.stock.entity;

import java.util.Date;

import com.psst.common.entity.BaseEntity;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午7:19:34
 * 类说明
 */
public class StockTransInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private long id = 0;
	private String stockName;
	private int stockId;
	private double openPrice;//开盘价
	private double oldPrice;//昨日收盘价
	private double realPrice;//当前价格
	private double maxPrice;//当日最高价
	private double minPrice;//当日最高价
	private long dealStocks;//成交数量
	private double dealMoney;//成交金额
	private int saleNum1;
	private double salePrice1;
	private int saleNum2;
	private double salePrice2;
	private int saleNum3;
	private double salePrice3;
	private int saleNum4;
	private double salePrice4;
	private int saleNum5;
	private double salePrice5;
	private int buyNum1;
	private double buyPrice1;
	private int buyNum2;
	private double buyPrice2;
	private int buyNum3;
	private double buyPrice3;
	private int buyNum4;
	private double buyPrice4;
	private int buyNum5;
	private double buyPrice5;
	
	private Date createTime;

	
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	public double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public long getDealStocks() {
		return dealStocks;
	}

	public void setDealStocks(long dealStocks) {
		this.dealStocks = dealStocks;
	}

	public double getDealMoney() {
		return dealMoney;
	}

	public void setDealMoney(double dealMoney) {
		this.dealMoney = dealMoney;
	}

	public int getSaleNum1() {
		return saleNum1;
	}

	public void setSaleNum1(int saleNum1) {
		this.saleNum1 = saleNum1;
	}

	public double getSalePrice1() {
		return salePrice1;
	}

	public void setSalePrice1(double salePrice1) {
		this.salePrice1 = salePrice1;
	}

	public int getSaleNum2() {
		return saleNum2;
	}

	public void setSaleNum2(int saleNum2) {
		this.saleNum2 = saleNum2;
	}

	public double getSalePrice2() {
		return salePrice2;
	}

	public void setSalePrice2(double salePrice2) {
		this.salePrice2 = salePrice2;
	}

	public int getSaleNum3() {
		return saleNum3;
	}

	public void setSaleNum3(int saleNum3) {
		this.saleNum3 = saleNum3;
	}

	public double getSalePrice3() {
		return salePrice3;
	}

	public void setSalePrice3(double salePrice3) {
		this.salePrice3 = salePrice3;
	}

	public int getSaleNum4() {
		return saleNum4;
	}

	public void setSaleNum4(int saleNum4) {
		this.saleNum4 = saleNum4;
	}

	public double getSalePrice4() {
		return salePrice4;
	}

	public void setSalePrice4(double salePrice4) {
		this.salePrice4 = salePrice4;
	}

	public int getSaleNum5() {
		return saleNum5;
	}

	public void setSaleNum5(int saleNum5) {
		this.saleNum5 = saleNum5;
	}

	public double getSalePrice5() {
		return salePrice5;
	}

	public void setSalePrice5(double salePrice5) {
		this.salePrice5 = salePrice5;
	}

	public int getBuyNum1() {
		return buyNum1;
	}

	public void setBuyNum1(int buyNum1) {
		this.buyNum1 = buyNum1;
	}

	public double getBuyPrice1() {
		return buyPrice1;
	}

	public void setBuyPrice1(double buyPrice1) {
		this.buyPrice1 = buyPrice1;
	}

	public int getBuyNum2() {
		return buyNum2;
	}

	public void setBuyNum2(int buyNum2) {
		this.buyNum2 = buyNum2;
	}

	public double getBuyPrice2() {
		return buyPrice2;
	}

	public void setBuyPrice2(double buyPrice2) {
		this.buyPrice2 = buyPrice2;
	}

	public int getBuyNum3() {
		return buyNum3;
	}

	public void setBuyNum3(int buyNum3) {
		this.buyNum3 = buyNum3;
	}

	public double getBuyPrice3() {
		return buyPrice3;
	}

	public void setBuyPrice3(double buyPrice3) {
		this.buyPrice3 = buyPrice3;
	}

	public int getBuyNum4() {
		return buyNum4;
	}

	public void setBuyNum4(int buyNum4) {
		this.buyNum4 = buyNum4;
	}

	public double getBuyPrice4() {
		return buyPrice4;
	}

	public void setBuyPrice4(double buyPrice4) {
		this.buyPrice4 = buyPrice4;
	}

	public int getBuyNum5() {
		return buyNum5;
	}

	public void setBuyNum5(int buyNum5) {
		this.buyNum5 = buyNum5;
	}

	public double getBuyPrice5() {
		return buyPrice5;
	}

	public void setBuyPrice5(double buyPrice5) {
		this.buyPrice5 = buyPrice5;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
