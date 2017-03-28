package com.psst.common.util;

import java.text.SimpleDateFormat;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午8:20:40
 * 类说明
 */
public class DateUtil {
	public static SimpleDateFormat sdfBase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat getBaseDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
}
