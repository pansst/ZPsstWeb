package com.psst.common.ftp.test;

import com.psst.common.ftp.FTPUtil;
import com.psst.common.log4j.Log4jUtil;

public class TestDownload {
    public static void testDowload() {
        try {
            long beginTime = System.currentTimeMillis();
            FTPUtil ftp = FTPUtil.getInstance();
            String path = "/systest";
            String localPath = "E:/systest_download";
            //localPath = "E:/systest";
            String[] listNames = ftp.listNames(path, false);
            int totalFile = listNames.length;
            int successFile = 0;
            int failFile = 0;
            for (String str : listNames) {
                Log4jUtil.info("FTP文件：" + str);
                String st = localPath + str.substring(str.lastIndexOf("/"));
                Log4jUtil.info("下载:" + st);
                boolean flag = ftp.get(str, st, false);
                if (flag) {
                    Log4jUtil.info("下载成功...");
                    successFile ++;
                } else {
                    Log4jUtil.info("下载失败....."); 
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
        testDowload();
    }
}
