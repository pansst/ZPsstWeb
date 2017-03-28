package com.psst.common.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.psst.common.log4j.Log4jUtil;


public class HTTPMethod {

	/**
	 * 日志类
	 */
	private static Logger log = Logger.getLogger(HTTPMethod.class);
	
	private static int TIME_OUT = 30000;
	private static String DEFAULT_CHARSET = "utf-8";
	/**
	 * put
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 */
	public static String doPutQuery(String url, String query, int time, String charSet){
	    try {
	        HttpEntity ret = HttpPool.httpPut(url, null, query);
	        String res = EntityUtils.toString(ret, charSet);
	        return res;
        } catch (Exception e) {
            Log4jUtil.error(log, e);
            log.error("【doPutQuery】error Info:[url: " + url + "]data:" + query);
        }
	    return null;
	    
		/*StringBuffer stringBuffer = new StringBuffer();
		HttpClient client = new HttpClient();
		PutMethod method = new PutMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/json;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
			if(200 == stateCode) {
                //log.debug("==============stateCode" + stateCode + "=======");
            } else {
                log.error("==============stateCode" + stateCode + "=======");
                log.error("【doPutQuery】error Info:[url: " + url + "]data:" + query);
            }
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									method.getResponseBodyAsStream(), "utf-8"));
					String str = "";
					while ((str = reader.readLine()) != null) {
						stringBuffer.append(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		method.abort();
		try {
			((SimpleHttpConnectionManager) client
					.getHttpConnectionManager()).shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();*/
	}
	
	public static String doPostQuery(String url, String query) throws HttpException {
		return doPostQuery(url, query, DEFAULT_CHARSET, TIME_OUT);
	}
	
	public static String doPostQuery(String url, String query, String charSet) throws HttpException {
		return doPostQuery(url, query, charSet, TIME_OUT);
	}
	/**
	 * post
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 * @throws HttpException
	 */
	public static String doPostQuery(String url, String query, String charSet, int time)
			throws HttpException {
	    try {
            HttpEntity ret = HttpPool.httpPost(url, null, query);
            String res = EntityUtils.toString(ret, charSet);
            return res;
        } catch (Exception e) {
            Log4jUtil.error(log, e);
            log.error("【doPostQuery】error Info:[url: " + url + "]data:" + query);
        }
        return null;
        
		/*StringBuffer stringBuffer = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/json;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
			if(200 == stateCode) {
			    //log.debug("==============stateCode" + stateCode + "=======");
			} else {
			    log.error("==============stateCode" + stateCode + "=======");
			    log.error("【doPostQuery】error Info:[url: " + url + "]data:" + query);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									method.getResponseBodyAsStream(), "utf-8"));
					String str = "";
					while ((str = reader.readLine()) != null) {
						stringBuffer.append(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			method.abort();
			try {
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();*/
	}
	public static String doGetQuery(String url) {
		return doGetQuery(url, DEFAULT_CHARSET, TIME_OUT);
	}
	public static String doGetQuery(String url, String charSet) {
		return doGetQuery(url, charSet, TIME_OUT);
	}
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String doGetQuery(String url, String charSet, int time) {
	    try {
            HttpEntity ret = HttpPool.httpGet(url, null);
            String res = EntityUtils.toString(ret, charSet);
            return res;
        } catch (Exception e) {
            Log4jUtil.error(log, e);
            log.error("【doGetQuery】error Info:[url: " + url + "]");
        }
        return null;
	   /* StringBuffer stringBuffer = new StringBuffer();
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Connection", "keep-alive");
        method.setRequestHeader(" Accept-Language", "zh-CN,zh;q=0.8");
        method.setRequestHeader(" Accept-Encoding", "gzip, deflate, sdch");
        method.setRequestHeader("Content-type",
                "application/json;charset=UTF-8");
        client.getHttpConnectionManager().getParams()
                .setConnectionTimeout(time);
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
        // 发出请求
        int stateCode = 0;
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            stateCode = client.executeMethod(method);
            if(200 == stateCode) {
                //log.debug("==============stateCode" + stateCode + "=======");
            } else {
                log.error("==============stateCode" + stateCode + "=======");
                log.error("【doGetQuery】error Info:[url: " + url + "]" );
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            if (stateCode == HttpStatus.SC_OK) {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    method.getResponseBodyAsStream(),"utf-8"));
                    String str = "";
                    while ((str = reader.readLine()) != null) {
                        stringBuffer.append(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            method.abort();
            try {
                ((SimpleHttpConnectionManager) client
                        .getHttpConnectionManager()).shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();    */       
	}
	
	/**
	 * delete
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 * @throws HttpException
	 */
	public static String doDeleteQuery(String url, int time)
			throws HttpException {
	    return "";
//		StringBuffer stringBuffer = new StringBuffer();
//		HttpClient client = new HttpClient();
//		DeleteMethod method = new DeleteMethod(url);
//		method.setRequestHeader("Connection", "close");
//		method.setRequestHeader("Content-type",
//				"application/json;charset=UTF-8");
//		client.getHttpConnectionManager().getParams()
//				.setConnectionTimeout(time);
//		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
//		/*try {
//			RequestEntity requestEntity = new ByteArrayRequestEntity(
//					query.getBytes("UTF-8"), "UTF-8");
//			method.setRequestEntity(requestEntity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}*/
//		// 发出请求
//		int stateCode = 0;
//		StopWatch stopWatch = new StopWatch();
//		try {
//			stopWatch.start();
//			stateCode = client.executeMethod(method);
//			if(200 == stateCode) {
//	                //log.debug("==============stateCode" + stateCode + "=======");
//            } else {
//                log.error("==============stateCode" + stateCode + "=======");
//                log.error("【doGetQuery】error Info:[url: " + url + "]" );
//            }
//		} catch (HttpException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			stopWatch.stop();
//			if (stateCode == HttpStatus.SC_OK) {
//				try {
//					BufferedReader reader = new BufferedReader(
//							new InputStreamReader(
//									method.getResponseBodyAsStream(), "utf-8"));
//					String str = "";
//					while ((str = reader.readLine()) != null) {
//						stringBuffer.append(str);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			method.abort();
//			try {
//				((SimpleHttpConnectionManager) client
//						.getHttpConnectionManager()).shutdown();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return stringBuffer.toString();
	}
}
