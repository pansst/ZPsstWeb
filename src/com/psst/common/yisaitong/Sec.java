package com.psst.common.yisaitong;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用命令行 复制文件
 * @author shiyongsheng
 *
 */
public class Sec {
    public static Map<String,String> extMap = new HashMap<String,String>();
    public static Map<String,String> extMap2 = new HashMap<String,String>();
    static{
        extMap.put("java", "ja");
        extMap2.put("ja", "java");
        
        extMap.put("html", "ht");
        extMap2.put("ht", "html");
        
        extMap.put("jsp", "jrp");
        extMap2.put("jrp", "jsp");
    }
    public static void copyFile(String fromAbs, String toAbs) throws IOException {
        // "cmd /c copy Test.c Test.txt" 
        String cmd = "cmd /c copy " + fromAbs + " " + toAbs; 
        Runtime runtime = Runtime.getRuntime(); 
        Process process = runtime.exec(cmd);
        System.out.println("处理文件" + fromAbs +"  变化为" + toAbs);
        //process.destroy();  会复制不成功
       
    }
    /**
     * 过滤并复制文件 替换后缀名
     * @param fileSource
     * @param extMaps
     * @throws IOException
     */
    public static void copy(File fileSource, Map<String,String> extMaps) throws IOException {
        if(fileSource.isDirectory()) {
            for(File file : fileSource.listFiles()) {
                copy(file, extMaps);
            }
        } else {
            String fileName = fileSource.getName();
            String path = fileSource.getAbsolutePath();
            String newPath = path.substring(0, path.lastIndexOf("\\")) + "\\";
            String targetFileName = "";
            if(fileName.indexOf(".") > 0) {
                String ext = fileName.substring(fileName.indexOf(".") + 1);
                if(extMaps.containsKey(ext)) {
                    targetFileName = fileName.substring(0, fileName.indexOf(".")) + "." + extMaps.get(ext);
                    newPath += targetFileName;
                    copyFile(path, newPath);
                    fileSource.deleteOnExit();
                } 
            }
        }
    }
    public static void copy(String pathSource, Map<String,String> extMaps) throws IOException {
        File fSource = new File(pathSource);
        copy(fSource, extMaps);
    }
    public static void main(String[] args) {
        try {
           //copy("E:\\psst",extMap);
           copy("E:\\BaiduNetdiskDownload\\sap3.0_2\\sap3.0_2",extMap2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
