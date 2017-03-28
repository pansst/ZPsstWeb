package com.psst.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.network.HTTPMethod;
import com.psst.stock.entity.StockInfo;
import com.psst.stock.entity.StockTransInfo;

/**
 * 抓取 新浪 股票信息
 * @author shiyongsheng
 *
 */
public class StockUtil {
    private static String url = "http://hq.sinajs.cn/list=sh600939,sz300534";//sz000830,s_sh000001,s_sz399001,s_sz399106";
    private static final String GET_TRANS_INFO_URL = "http://hq.sinajs.cn/list=";
    private static final String STOCK_LIST_URL = "http://quote.eastmoney.com/stocklist.html";
    public static void getInfo(String url){
        try {
            URL u = new URL(url);
            byte[] buffer = new byte[256];
            char[] buffer2 = new char[256];
            InputStream input = null;
            InputStreamReader reader = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while(true){
                try {
                    input = u.openStream();
                    //reader = new InputStreamReader(input, "gb2312");
                    String str = null;
                    
                    int len = 0;
                    /*StringBuilder builder = new StringBuilder();
                    while((len = reader.read(buffer2)) != -1){
                        builder.append(buffer2,0,len);
                    }
                    System.out.println(builder.toString());*/
                    
                    while((len = input.read(buffer)) != -1){
                        output.write(buffer, 0, len);
                    }
                    String reult = output.toString("gb2312");
                    System.out.println(reult);
                    String[] stocks = reult.split(";");
                    for(String stock : stocks) {
                        //String[] datas = stock.split(",");
                        //System.out.println(stock);
                    }
                    output.reset();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if(null != input) {
                        input.close();
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 获取列表
     * @return
     */
    public static String getStockListByHttp() {
    	String content = HTTPMethod.doGetQuery(STOCK_LIST_URL, "gb2312");
    	//System.out.println(content);
    	//Map<String,Object> ret = new HashMap<String,Object>();
    	//int index = content.indexOf("<body>");
    	//int endIndex = content.lastIndexOf("</body>");
    	//content = content.substring(index + 6, endIndex).trim();
    	//ret.put("content", content);
    	//System.out.println(content);
    	//ret.put("content", "11111");
    	return content;
    }
    public static Map<String, String> getStockMapByHttp() {
    	return parseList(getStockListByHttp());
    }
    public static Map<String, String> parseList(String str) {
    	Map<String, String> ret = new HashMap<String, String>();
    	while(true){
    		int beginIndex = str.indexOf("<li><a target=");
    		int endIndex  = str.indexOf("</a></li>", beginIndex);
    		if(beginIndex <= -1 || endIndex <= -1) {
    			break;
    		}
    		String con = str.substring(beginIndex, endIndex);
    		beginIndex = con.lastIndexOf(">");
    		con = con.substring(beginIndex + 1);
    		//System.out.println(con);
    		beginIndex = con.indexOf("(");
    		if(beginIndex > 0) {
    			String name = con.substring(0, beginIndex);
        		String code = con.substring(beginIndex + 1, con.length() - 1);
        		if(code.length() == 6 && (code.startsWith("0") || code.startsWith("6") || code.startsWith("3"))) {
            		//System.out.println(name + ":" + code);
            		ret.put(code, name);
        		}
    		}
    		str = str.substring(endIndex);
    	}
    	return ret;
    }
    
    public static List<StockTransInfo> getTransInfo(List<StockInfo> stockInfoList, SimpleDateFormat sdf) {
    	StringBuilder sb = new StringBuilder(GET_TRANS_INFO_URL);
    	Map<String,Integer> ret = new HashMap<String, Integer>();
    	for(StockInfo stockInfo : stockInfoList){
    		String typeStr = stockInfo.getTypeStr() + stockInfo.getStockCode();
    		sb.append(typeStr).append(",");
    		ret.put(typeStr,stockInfo.getId());
    	}
    	if(sb.charAt(sb.length() - 1) == ',') {
    		sb.deleteCharAt(sb.length() - 1);
    	}
    	String content = HTTPMethod.doGetQuery(sb.toString(), "gb2312");
    	List<StockTransInfo> listTrans = parseTransInfo(ret, content, sdf);
    	//System.out.println(content);
    	return listTrans;
    }
    public static List<StockTransInfo> parseTransInfo(Map<String,Integer> stockCodeIdMaps, String content, SimpleDateFormat sdf) {
    	String[] cons = content.trim().split(";");
    	List<StockTransInfo> ret = new ArrayList<StockTransInfo>();
    	for(String con : cons) {
    		con = con.trim();
    		try {
        		String code = con.substring("var hq_str_".length(), con.indexOf("="));
        		StockTransInfo stockTransInfo = new StockTransInfo();
        		stockTransInfo.setId(-1);//默认为垃圾数据
        		ret.add(stockTransInfo);
        		stockTransInfo.setStockId(stockCodeIdMaps.get(code));
        		String[] datas = con.substring(con.indexOf("\"") + 1, con.length() - 1).split(",");
        		stockTransInfo.setStockName(datas[0]);
        		stockTransInfo.setOpenPrice(Double.parseDouble(datas[1]));
        		stockTransInfo.setOldPrice(Double.parseDouble(datas[2]));
        		stockTransInfo.setRealPrice(Double.parseDouble(datas[3]));
        		stockTransInfo.setMaxPrice(Double.parseDouble(datas[4]));
        		stockTransInfo.setMinPrice(Double.parseDouble(datas[5]));
        		stockTransInfo.setDealStocks(Long.parseLong(datas[8]));
        		stockTransInfo.setDealMoney(Double.parseDouble(datas[9]));
        		stockTransInfo.setBuyNum1(Integer.parseInt(datas[10]));
        		stockTransInfo.setBuyPrice1(Double.parseDouble(datas[11]));
        		stockTransInfo.setBuyNum2(Integer.parseInt(datas[12]));
        		stockTransInfo.setBuyPrice2(Double.parseDouble(datas[13]));
        		stockTransInfo.setBuyNum3(Integer.parseInt(datas[14]));
        		stockTransInfo.setBuyPrice3(Double.parseDouble(datas[15]));
        		stockTransInfo.setBuyNum4(Integer.parseInt(datas[16]));
        		stockTransInfo.setBuyPrice4(Double.parseDouble(datas[17]));
        		stockTransInfo.setBuyNum5(Integer.parseInt(datas[18]));
        		stockTransInfo.setBuyPrice5(Double.parseDouble(datas[19]));
        		stockTransInfo.setSaleNum1(Integer.parseInt(datas[20]));
        		stockTransInfo.setSalePrice1(Double.parseDouble(datas[21]));
        		stockTransInfo.setSaleNum2(Integer.parseInt(datas[22]));
        		stockTransInfo.setSalePrice2(Double.parseDouble(datas[23]));
        		stockTransInfo.setSaleNum3(Integer.parseInt(datas[24]));
        		stockTransInfo.setSalePrice3(Double.parseDouble(datas[25]));
        		stockTransInfo.setSaleNum4(Integer.parseInt(datas[26]));
        		stockTransInfo.setSalePrice4(Double.parseDouble(datas[27]));
        		stockTransInfo.setSaleNum5(Integer.parseInt(datas[28]));
        		stockTransInfo.setSalePrice5(Double.parseDouble(datas[29]));
        		String time  = datas[30] + " " + datas[31];
        		time = time.trim();
        		if (time.length() == 19) {
        			try {
        				//sdf需线程独享  多线程共享会报错
        				stockTransInfo.setCreateTime(sdf.parse(time));
        				stockTransInfo.setId(0);//正常数据
        			} catch (Exception e) {
        				Log4jUtil.error("时间解析出错[" + time + "]" ,e);
        			}
        		} else {
        			Log4jUtil.error("时间格式不对[" + time + "]");
        		}
			} catch (Exception e) {
				//Log4jUtil.error("数据解析出错[" + con + "]" ,e);
			}
    	}
    	return ret;
    }
    
    public static void main(String[] args) {
        //getInfo(STOCK_LIST_URL);
        //getInfo("http://www.baidu.com");
    	//getStockListByHttp();
    	/*StockInfo s1 = new StockInfo(1, "sh", "600939");
    	StockInfo s2 = new StockInfo(2, "sh", "603881");
    	StockInfo s3 = new StockInfo(3, "sz", "300395");
    	StockInfo s4 = new StockInfo(4, "sz", "002658");
    	StockInfo s5 = new StockInfo(5, "sh", "603628");
    	List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
    	stockInfoList.add(s1);
    	stockInfoList.add(s2);
    	stockInfoList.add(s3);
    	stockInfoList.add(s4);
    	stockInfoList.add(s5);
    	getTransInfo(stockInfoList);*/
    	
    	String st = "2000-03-23 00:26:33";
    	try {
			Date d = DateUtil.sdfBase.parse(st);
			System.out.println(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
