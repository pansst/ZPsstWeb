package com.psst.common.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Title: ListSort.java
 * @Package com.sinovoice.hcicloud.common
 * @Description: List对象排序
 * @author wudong
 * @date 2015年4月17日 下午6:29:03
 * @param <E>
 */
@SuppressWarnings("all")
public class ListSort<E> {
	/**
	 * @Title: Sort
	 * @Description: list对象排序
	 * @param list
	 * @param method
	 * @param sort
	 * @throws
	 */
	public void Sort(List<E> list, final String method, final String sort) {
		// 用内部类实现排序
		Collections.sort(list, new Comparator<E>() {

			@Override
			public int compare(E a, E b) {
				int ret = 0;
				try {
					// 获取m1的方法名
					Method m1 = a.getClass().getMethod(method, null);
					// 获取m2的方法名
					Method m2 = b.getClass().getMethod(method, null);
					if (sort != null && "desc".equals(sort)) {
						ret = m2.invoke((b), null).toString()
								.compareTo(m1.invoke((a), null).toString());
					} else {
						// 正序排序
						ret = m1.invoke((a), null).toString()
								.compareTo(m2.invoke((b), null).toString());
					}
				} catch (Exception e) {

				}
				return ret;
			}
		});
	}
}
