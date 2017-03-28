package com.psst.common.ftp.test;

import java.io.File;

import com.psst.common.ftp.FTPUtil;
import com.psst.common.log4j.Log4jUtil;

public class TestUpload {
    public static void testUpload() {
        try {
            long beginTime = System.currentTimeMillis();
            FTPUtil ftp = FTPUtil.getInstance();
            String path = "/systest/";
            String localPath = "E:/systest_download";
            String[] listNames = new File(localPath).list();
            int totalFile = listNames.length;
            int successFile = 0;
            int failFile = 0;
            for (String str : listNames) {
                Log4jUtil.info("本地文件：" + localPath + File.separator + str);
                String st = path + str;
                Log4jUtil.info("上传:" + st);
                boolean flag = ftp.put(st, localPath + File.separator + str, false);
                if (flag) {
                    Log4jUtil.info("上传成功...");
                    successFile ++;
                } else {
                    Log4jUtil.info("上传失败....."); 
                    failFile ++;
                }
                if(successFile > 0 && successFile % 5 == 0) {
                    Log4jUtil.info("文件总数:" + totalFile + "\t 成功:" + successFile + "\t失败:" + failFile);
                }
            }
            Log4jUtil.info("结束.........\n文件总数:" + totalFile + "\t 成功:" + successFile + "\t失败:" + failFile);
            long endTime = System.currentTimeMillis();
            Log4jUtil.info("总共耗时:" + (endTime - beginTime) + "毫秒");
        } catch (Exception e) {
            Log4jUtil.error(e);
        }
    }
    public static void main(String[] args) {
        testUpload();
    }
}
