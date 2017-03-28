package com.psst.stock.service.task;

import org.springframework.stereotype.Service;

import com.psst.stock.global.GlobalConst;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-27 下午6:47:09
 * 任务调度
 */
@Service
public class TaskSchudleTask {
	/**
	 * 上午初次调用
	 */
	public void beginGetStockTransInfoAM() {
		GlobalConst.isBatchEnd =  false;
		InitInfoTask.getInstance().beginDeal();
	
	}
	/**
	 *下午调用
	 */
	public void beginGetStockTransInfoPM() {
		GlobalConst.isBatchEnd =  false;
		InitInfoTask.getInstance().beginDeal();
	}
	/**
	 * 结束
	 */
	public void endGetStockTransInfo() {
		GlobalConst.isBatchEnd =  true;
	}
}
