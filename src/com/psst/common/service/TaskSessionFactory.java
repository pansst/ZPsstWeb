package com.psst.common.service;

import org.hibernate.SessionFactory;

/**
 * 获取SessionFactory对象
 * @author panxudong
 *
 */
public class TaskSessionFactory {
	
	private static SessionFactory sessionFactory;
	
	public TaskSessionFactory() {
		
	}

	/**
	 * 获取sessionFactory
	 * @return the sessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 设置sessionFactory
	 * @param sessionFactory the sessionFactory to set
	 */
	public static void setSessionFactory(SessionFactory sessionFactory) {
	    //System.out.println("---------------------------------注入sessionFactory------------------");
		TaskSessionFactory.sessionFactory = sessionFactory;
	}
	
	
}
