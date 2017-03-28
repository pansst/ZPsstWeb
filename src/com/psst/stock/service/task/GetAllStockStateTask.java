package com.psst.stock.service.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.service.task.TaskInterface;
import com.psst.common.util.DateUtil;
import com.psst.common.util.StockUtil;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.entity.StockTransInfo;
import com.psst.stock.global.GlobalConst;
import com.psst.stock.service.StockServiceI;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 下午7:24:43
 * 
 */
public class GetAllStockStateTask implements TaskInterface{
	@Autowired
	private StockServiceI stockService;
	
	private static GetAllStockStateTask instance;
	
	public static GetAllStockStateTask getInstance() {
		return instance;
	}
	
	public void dealStockStateByTransInfo(StockTransInfo transInfo) {
		
	}
	private void updateStockInfo(List<StockInfo> stockInfoList) {
		if (null == stockInfoList) {
			return;
		}
		for (StockInfo stockInfo : stockInfoList) {
			updateStockInfo(stockInfo);
		}
	}
	private void updateStockInfo(StockInfo stockInfo) {
		stockService.updateStockInfo(stockInfo);
	}
	@Override
	public void run() {
		List<StockInfo> list = stockService.getAllStockInfo();
		int st = 0;
		int normal = 0;
		int delete = 0;
		int stop = 0;
		int size = 0;
		for (StockInfo stock : list) {
			List<StockInfo> stockInfoList = new ArrayList<StockInfo>(); 
			stockInfoList.add(stock);
			List<StockTransInfo> transList = StockUtil.getTransInfo(stockInfoList, DateUtil.getBaseDateFormat());
			if (transList.size() == 1) {
				StockTransInfo transInfo = transList.get(0);
				if (transInfo.getId() == -1) {
					stock.setState(GlobalConst.STOCK_STATE_DELETE);
					stock.setUpdateTime(new Date());
					delete ++;
				} else {
					stock.setStockName(transInfo.getStockName());
					if (transInfo.getOpenPrice() == 0) {
						//停牌
						stock.setState(GlobalConst.STOCK_STATE_STOP);
						stock.setUpdateTime(new Date());
						stop ++;
					} else if (transInfo.getStockName().startsWith("*") || transInfo.getStockName().startsWith("ST")){//ST
						stock.setState(GlobalConst.STOCK_STATE_ST);
						stock.setUpdateTime(new Date());
						st++;
					} else {
						normal ++;
						stock.setState(GlobalConst.STOCK_STATE_NORMAL);
						stock.setUpdateTime(new Date());
					}
				}
			}
			size ++;
			if ((size +1) % 50 == 0) {
				Log4jUtil.info("已处理" + size + "条数据..");
			}
		}
		updateStockInfo(list);
		/*for(StockInfo stock : list) {
			stockService.updateStockInfo(stock);
		}*/
		Log4jUtil.info("更新完毕.......st:" + st + " normal:" + normal + " stop:" + stop +" delete:" + delete);
	}
	

}
