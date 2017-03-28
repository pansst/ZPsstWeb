package com.psst.stock.global;

import java.util.Calendar;

import com.psst.stock.service.task.InitInfoTask;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-23 上午10:50:29
 * 全局配置
 */
public class GlobalConst {
	/**
	 * 缓存stock列表
	 */
	public static final String REDIS_KEY_STORE_STOCK_LIST = "stock_list";
	
	/**
	 * trans info数据列表
	 */
	public static final String REDIS_KEY_STOCK_TRANSINFO_LIST = "stock_trans_list";
	
	/**
	 * trans info保存列表
	 */
	public static final String REDIS_KEY_STORE_STOCK_TRANSINFO_LIST = "store_stock_trans_list_";
	
	/**
	 * 记录线程信息
	 */
	public static final String REDIS_KEY_GET_TRANSINFO_THREAD_LIST = "trans_info_batch_thread_list";
	/**
	 * 获取stock trans info 列表
	 */
	public static final String REDIS_KEY_GET_STOCK_TRANSINFO_BACTH_LIST = "get_stock_trans_batch_list_";
	
	/**
	 * 垃圾信息
	 */
	public static final String REDIS_KEY_BAD_STOCK_LIST = "stock_bad_list";
	/**
	 * http请求每次数量
	 */
	public static final int BATCH_GET_TRANS_INFO_SIZE = 200;
	/**
	 * 最多抓取数量
	 */
	public static final int BATCH_GET_TRANS_INFO_LIST_SIZE = 1000;
	/**
	 * 保存线程每次提交最大数量
	 */
	public static final int BTRANS_INFO_STOR_BATCH_SIZE = 300;
	/**
	 * 抓取线程睡眠时间
	 */
	public static final long BACTH_SLEEP_TIME = 1000;

	/**
	 * stock正常状态
	 */
	public static final int STOCK_STATE_NORMAL = 1;
	/**
	 * stock ST状态
	 */
	public static final int STOCK_STATE_ST = 2;
	/**
	 * stock已经删除状态
	 */
	public static final int STOCK_STATE_DELETE = -1;
	/**
	 * stock 暂停状态
	 */
	public static final int STOCK_STATE_STOP = 3;
	
	
	/**
	 * 是否应该结束
	 */
	public static boolean isBatchEnd = false;
	
	/**
	 * 是否结束完毕
	 */
	public static boolean isBatchEndDown = false;
	/**
	 * 其他条件  动态修改
	 */
	public static boolean otherCondition = false;
	/**
	 * 测试状态   true 不检验时间  false  校验时间等其他条件
	 */
	public static  boolean testState = true;
	/**
	 * 是否可以运行抓取数据
	 * @return
	 */
	public static boolean canGetTransInfo() {
		/*if(!InitInfoTask.initDown) {
			return false;
		}*/
		if(testState && !isBatchEnd) {
			return true;
		}
		if (!otherCondition && !isBatchEnd) {
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			if(hour < 9 || hour > 15) {
				return false;
			}
			//9:15 - 11:30   13:00- 15:01
			if((hour == 9 && minute >= 15) || (hour > 9 && hour < 11) || (hour == 11 && minute < 30) || (hour >= 13 && hour < 15) || (hour == 15 && minute < 1)) {
				return true;
			}
		}
		return false;
	}
}
