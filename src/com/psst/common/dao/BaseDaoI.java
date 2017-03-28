package com.psst.common.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public abstract interface BaseDaoI<T> {
	public abstract Serializable save(T paramT);
	
	public abstract void saveList(List<T> list);
	
	public abstract Serializable saveNow(T paramT);

	public abstract void delete(T paramT);
	
	public abstract void update(T paramT);
		
	public abstract void saveOrUpdate(T paramT);

	public abstract T get(Class<T> paramClass, Serializable paramSerializable);

	public abstract T get(String paramString);

	public abstract T get(String paramString, Map<String, Object> paramMap);

	public abstract List<T> find(String paramString);

	public abstract List<T> find(String paramString,
			Map<String, Object> paramMap);

	public abstract List<T> find(String paramString, int paramInt1,
			int paramInt2);

	public abstract List<T> find(String paramString,
			Map<String, Object> paramMap, int paramInt1, int paramInt2);

	public abstract Long count(String paramString);

	public abstract Long count(String paramString, Map<String, Object> paramMap);

	public abstract int executeHql(String paramString);

	public abstract int executeHql(String paramString, Map<String, Object> paramMap);

	public abstract List<Object[]> findBySql(String paramString);

	public abstract List<Object[]> findBySql(String paramString, int paramInt1,
			int paramInt2);

	public abstract List<Object[]> findBySql(String paramString,
			Map<String, Object> paramMap);

	public abstract List<Object[]> findBySql(String paramString,
			Map<String, Object> paramMap, int paramInt1, int paramInt2);

	public abstract int executeSql(String paramString);

	public abstract int executeSql(String paramString,
			Map<String, Object> paramMap);

	public abstract BigInteger countBySql(String paramString);

	public abstract BigInteger countBySql(String paramString,
			Map<String, Object> paramMap);
}

/*
 * Location: D:\Program Files\apache-tomcat-7.0.52\webapps\sap\WEB-INF\classes\
 * Qualified Name: com.hcicloud.sap.dao.BaseDaoI JD-Core Version: 0.6.1
 */