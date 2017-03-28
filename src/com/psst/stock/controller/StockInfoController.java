package com.psst.stock.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.psst.common.controller.BaseController;
import com.psst.common.util.StockUtil;
import com.psst.stock.service.StockServiceI;

/**
 * @author yongsheng.shi
 * @version 创建时间：2017-3-22 下午1:45:50
 * 类说明
 */
@Controller
@RequestMapping({ "/stockInfo" })
public class StockInfoController extends BaseController{
	@Autowired
	private StockServiceI stockService;
	
	@RequestMapping({ "/manager" })
	public String manager(HttpServletRequest request) {
		return "/stockInfo";
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getStockInfoByHttp",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getStockInfoByHttp(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		response.setHeader( "Access-Control-Allow-Origin", "*");
		response.setHeader( "Access-Control-Allow-Methods", "POST");
		response.setHeader( "Access-Control-Allow-Headers", "x-requested-with,content-type" );
		//return StockUtil.getStockListByHttp();
		return null;
	}
}
