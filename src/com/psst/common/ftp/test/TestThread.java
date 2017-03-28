package com.psst.common.ftp.test;

public class TestThread {
    public static void main(String[] args) {
        /**
         * 下载
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                TestDownload.testDowload();
            }
        }).start();;
        /**
         * 上传
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
               TestUpload.testUpload();
            }
        }).start();
    }
}
