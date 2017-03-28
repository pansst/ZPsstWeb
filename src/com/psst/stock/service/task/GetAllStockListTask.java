package com.psst.stock.service.task;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.util.StockUtil;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.service.StockServiceI;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午5:22:57
 *获取stock列表
 */
@Service
public class GetAllStockListTask {
	private static int[] types = {0,0,0,3,0,0,6};
	private static String[] typeStrs = {"sz","","","sz","","","sh","",""};
	@Autowired
	private StockServiceI stockService;
	public void updateRun(){
		Log4jUtil.info("开始拉取stock远程列表信息");
		Map<String, String> listMap = StockUtil.getStockMapByHttp();
		Log4jUtil.info("拉取stock远程列表信息完毕，开始处理");
		Set<String> codeSet = listMap.keySet();
		Set<String> codeSortSet = new TreeSet<String>(codeSet);
		for(String stockCode : codeSortSet) {
			String stockName = listMap.get(stockCode);
			StockInfo stockInfo = new StockInfo();
			stockInfo.setStockCode(stockCode);
			stockInfo.setStockName(stockName);
			stockInfo.setUpdateTime(new Date());
			int type = stockCode.charAt(0) - 48;
			stockInfo.setType(types[type]);
			stockInfo.setTypeStr(typeStrs[type]);
			stockService.addStockInfo(stockInfo);
		}
		Log4jUtil.info("stock远程列表信息处理完毕");
	}
}
