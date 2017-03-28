package com.psst.common.log4j;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * log4j调用工具
 * @author shiyongsheng
 *
 */
public class Log4jUtil {
    /**
     * 获得一个Logger
     * @return
     */
    public static Logger getLogger(Class clzz) {
        //Logger logger = Logger.getLogger(Reflection.getCallerClass());
        Logger logger = Logger.getLogger(clzz);
        return logger;
    }
    
    /**
     * debug级别信息
     * @param message
     */
    public static void debug(Class clzz, String message) {
        Logger logger = Logger.getLogger(clzz);
        logger.debug(message);
    }
    /**
     * debug级别信息
     * @param message
     */
    public static void debug(String message) {
        Logger logger = Logger.getLogger(getCallInfo());
        logger.debug(message);
    }
    /**
     * info级别信息
     * @param message
     */
    public static void info(Class clzz, String message) {
        Logger logger = Logger.getLogger(clzz);
        logger.info(message);
    }
    /**
     * info级别信息
     * @param message
     */
    public static void info(String message) {
        Logger logger = Logger.getLogger(getCallInfo());
        logger.info(message);
    }
    /**
     * warn级别信息
     * @param message
     */
    public static void warn(Class clzz, String message) {
        //Logger logger = Logger.getLogger(Reflection.getCallerClass());
        Logger logger = Logger.getLogger(clzz);
        logger.warn(message);
    }
    
    /**
     * warn级别信息
     * @param message
     */
    public static void warn(String message) {
        Logger logger = Logger.getLogger(getCallInfo());
        logger.warn(message);
    }
    
    /**
     * error级别信息
     * @param message
     */
    public static void error(Class clzz, String message) {
        Logger logger = Logger.getLogger(clzz);
        logger.error(message);
    }
    
    /**
     * error级别信息
     * @param message
     */
    public static void error(String message) {
        Logger logger = Logger.getLogger(getCallInfo());
        logger.error(message);
    }
    public static void error(String message, Exception e) {
        Logger logger = Logger.getLogger(getCallInfo());
        logger.error(message);
        error(logger, e);
    }
    public static void error(Exception e) {
        Logger logger = Logger.getLogger(getCallInfo());
        try {
            logger.error(getErrorInfoFromException(e));
        } catch (Exception e2) {
            e2.printStackTrace();
            logger.error("处理error信息失败..." + e2.getMessage());
        }
    }
    /**
     * error级别信息
     * @param message
     */
    public static void error(Logger logger,String message) {
        //Logger logger = Logger.getLogger(Reflection.getCallerClass());
        logger.error(message);
    }
    /**
     * 根据Exception 输出error信息
     * @param e
     */
    public static void error(Class clzz,Exception e) {
        //Logger logger = Logger.getLogger(Reflection.getCallerClass());
        Logger logger = Logger.getLogger(clzz);
        try {
            logger.error(getErrorInfoFromException(e));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    /**
     * 根据Exception 输出error信息
     * @param e
     */
    public static void error(Logger logger,Exception e) {
        //Logger logger = Logger.getLogger(Reflection.getCallerClass());
        try {
            logger.error(getErrorInfoFromException(e));
        } catch (Exception e2) {
            e2.printStackTrace();
            logger.error("处理error信息失败..." + e2.getMessage());
        }
    }
    /**
     * 获得Exception 的字符串描述
     * @param e
     * @return
     */
    public static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "bad getErrorInfoFromException";
        }
    }
    
    private static String getCallInfo() {
        return getCallInfo(3);
    }
    private static String getCallInfo(int index) {
        String info = "";
        try {
            StackTraceElement[] elements = new Throwable().getStackTrace();
            info = elements[index].getClassName() + "." + elements[index].getMethodName() + "[line: " + elements[index].getLineNumber()+ "]"; 
        } catch (Exception e) {
            e.printStackTrace();
            info = "获取调用者信息出错...." + e.getMessage();
        }
        return info;
    }
    public static void test(String message) {
        Logger logger = Logger.getLogger(getCallInfo());
        logger.debug(message);
       /* String className = "";
        String methodName = "";
        StackTraceElement[] elements = new Throwable().getStackTrace();
        for (int i = 0; i < elements.length; i++){
            //if (this.getClass().getName().equals(elements[i].getClassName())){
               // 获取堆栈的下一个元素，就是调用者元素
               // 如果想要获取当前方法所在类的信息，直接读取elements[i]就可以了
               className = elements[i].getClassName();
               methodName = elements[i].getMethodName();
//               break;
//            }
              System.out.println(className + "   【" + methodName + "】\n");
         }*/
        
    }
}
