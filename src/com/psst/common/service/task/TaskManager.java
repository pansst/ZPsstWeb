package com.psst.common.service.task;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.service.SpringManager;
import com.psst.common.util.CommonMethod;


/**
 * 任务调度
 * @author panxudong
 *
 */
@SuppressWarnings("unchecked")
public class TaskManager {
    static ApplicationContext ac ;
    
	public static void begin() {
	    ac = SpringManager.getApplicationContext();
	    SAXReader reader = new SAXReader();    
		Document  document = null;
		List<Element> elements  = null;
		TaskInterface in = null;
	    try {
	        //解析bean.xml
		    document = reader.read(CommonMethod.getPath()+"/WEB-INF/classes/bean.xml");
            Element node = document.getRootElement();
		    elements = node.elements();
		    //遍历bean.xml中的bean创建线程
		    for (Element element : elements) {
		    	try {
		    		in = (TaskInterface) ac.getBean(element.attributeValue("id"));
				    //System.out.println(in.getClass().getName());
		            new Thread(in).start(); 
				} catch (Exception e) {
					Log4jUtil.error(e);
				}
		    }
        } catch (Exception e) {
            Log4jUtil.error(e);
        }
	}
}
