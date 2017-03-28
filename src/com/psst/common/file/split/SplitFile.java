package com.psst.common.file.split;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 拆分大文件合并小文件工具
 * @author shiyongsheng
 *
 */
public class SplitFile {
    /**
     * 单个小文件大小 100M
     */
    private static long LITTE_SIZE = 100 * 1024 * 1024;
    /**
     * 拆分文件最小要求是10配小文件大小 100M
     */
    private static long SPLIE_FILE_MIN_SIZE = 1 * LITTE_SIZE;

    public static long getLITTE_SIZE() {
        return LITTE_SIZE;
    }

    public static void setLITTE_SIZE(long lITTE_SIZE) {
        LITTE_SIZE = lITTE_SIZE;
    }

    public static long getSPLIE_FILE_MIN_SIZE() {
        return SPLIE_FILE_MIN_SIZE;
    }

    public static void setSPLIE_FILE_MIN_SIZE(long sPLIE_FILE_MIN_SIZE) {
        SPLIE_FILE_MIN_SIZE = sPLIE_FILE_MIN_SIZE;
    }

    /**
     * 将指定目录的大文件全部拆分为小文件
     * 
     * @param bigFileDirectory
     */
    public static void splitFileByDirectory(String bigFileDirectory) {
        splitFileByDirectory(bigFileDirectory, new FileFilter() {
            @Override
            public boolean accept(File arg0) {
                return arg0.length() > SPLIE_FILE_MIN_SIZE;
            }
        });
    }

    /**
     * 将指定目录的符合条件的大文件全部拆分为小文件
     * 
     * @param bigFileDirectory
     * @param fileFilter
     */
    public static void splitFileByDirectory(String bigFileDirectory,
            FileFilter fileFilter) {
        try {
            System.out.println("开始扫描目录【" + bigFileDirectory + "】.........");
            File directory = new File(bigFileDirectory);
            if (directory.isDirectory()) {
                File[] files = directory.listFiles(fileFilter);
                for (File f : files) {
                    splitFile(f);
                }
                System.out.println("拆分大文件完毕，共拆分" + files.length
                        + "个大文件.........");
            } else {
                System.out.println("目标不是一个目录。。。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拆分指定大文件
     * 
     * @param bigFile
     */
    public static void splitFile(File bigFile) {
        SplitConfig cfg = null;
        if (bigFile.exists() && bigFile.isFile()
                && bigFile.length() > SPLIE_FILE_MIN_SIZE) {
            cfg = new SplitConfig();
            List<String> littleNames = new ArrayList<String>();

            // 大文件名名册
            cfg.setBigFileName(bigFile.getName());
            Long size = bigFile.length();
            long num = (size - 1) / LITTE_SIZE + 1;
            for (int index = 0; index < num; index++) {
                String name = System.currentTimeMillis() + "_" + index + ".bak";
                littleNames.add(name);
            }
            // 大文件大小
            cfg.setBigFileSize(size);
            // 小文件文件名列表
            cfg.setLittleFiles(littleNames);
            // 开始拆分
            String p = bigFile.getAbsolutePath();
            String littleFilePath = p.substring(0,
                    p.lastIndexOf(File.separator))
                    + File.separator;
            List<OutputStream> outputStreamList = new ArrayList<OutputStream>();
            byte[] buffer = new byte[1024];
            Long readyReadSize = 0L;
            try {
                /**
                 * 写入配置文件
                 */
                String cfgFilePath = littleFilePath
                        + System.currentTimeMillis() + ".cfg";
                File cfgFile = new File(cfgFilePath);
                if (cfgFile.exists()) {
                    cfgFile.createNewFile();
                }
                ObjectOutputStream objOutput = new ObjectOutputStream(
                        new FileOutputStream(cfgFile));
                objOutput.writeObject(cfg);
                objOutput.close();
                System.out.println("序列化配置信息完毕，文件名为" + cfgFilePath);

                for (String name : littleNames) {
                    File liFile = new File(littleFilePath + name);
                    liFile.createNewFile();
                    liFile.setWritable(true);
                    OutputStream outputStream = new FileOutputStream(liFile);
                    outputStreamList.add(outputStream);
                }
                InputStream input = new FileInputStream(bigFile);
                int len = 0;
                while ((len = input.read(buffer)) > 0) {
                    readyReadSize += len;
                    int index = (int) ((readyReadSize - 1) / LITTE_SIZE);
                    outputStreamList.get(index).write(buffer, 0, len);
                    if (readyReadSize % LITTE_SIZE == 0) {
                        outputStreamList.get(index).close();
                        System.out.println("写入第" + (index + 1) + "个文件完毕，文件名为"
                                + littleNames.get(index));
                    }
                }
                input.close();
                System.out.println(bigFile.getName() + "写入完毕...........");
                bigFile.delete();
                System.out.println(cfg.getBigFileName()
                        + "原始大文件删除完毕...........");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将指定路径的配置文件反序列化，合并小文件
     * 
     * @param cfgPath
     */
    public static void mergeBigFile(String cfgPath) {
        try {
            File cfgFile = new File(cfgPath);
            mergeFile(cfgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将指定配置文件解析，合并文件
     * 
     * @param cfgFile
     */
    public static void mergeFile(File cfgFile) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(cfgFile));
            SplitConfig cfg = (SplitConfig) objectInputStream.readObject();
            objectInputStream.close();
            System.out.println(cfgFile.getName() + "反序列化配置文件完毕...........");
            cfgFile.delete();
            System.out.println("配置文件已删除...............");
            String p = cfgFile.getAbsolutePath();
            String directoryPath = p
                    .substring(0, p.lastIndexOf(File.separator))
                    + File.separator;

            String bigFilePath = directoryPath + cfg.getBigFileName();
            File bigFile = new File(bigFilePath);
            bigFile.createNewFile();

            OutputStream outputStream = new FileOutputStream(bigFile);
            byte[] buffer = new byte[1024];
            int len = 0;
            List<String> littleNames = cfg.getLittleFiles();
            for (String name : littleNames) {
                File f = new File(directoryPath + name);
                InputStream inputStream = new FileInputStream(f);
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                System.out.println("小文件" + name + "写入完毕......");
                f.delete();
                System.out.println("小文件" + name + "删除完毕......");
            }
            outputStream.close();
            System.out.println("文件合并完毕，文件名为" + bigFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在一个目录下合并文件 配置文件已cfg结尾
     * 
     * @param direcotry
     */
    public static void mergeFileByDirectory(String direcotry) {
        mergeFileByDirectory(direcotry, new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("cfg");
            }
        });
    }

    /**
     * 在一个目录下合并文件，根据filter挑选出cfg文件
     * 
     * @param direcotry
     * @param filenameFilter
     */
    public static void mergeFileByDirectory(String direcotry,
            FilenameFilter filenameFilter) {
        try {
            System.out.println("开始扫描目录【" + direcotry + "】.........");
            File directory = new File(direcotry);
            if (directory.isDirectory()) {
                File[] files = directory.listFiles(filenameFilter);
                for (File f : files) {
                    mergeFile(f);
                }
                System.out.println("处理配置文件完毕，共处理" + files.length
                        + "个配置文件.........");
            } else {
                System.out.println("目标不是一个目录。。。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //splitFileByDirectory("D:/test");
        mergeFileByDirectory("F:\\BaiduYunDownload\\centos-6.5");
        // splitFile("D:/test/eclipse-java-neon-1a-win32-x86_64.zip");
        // mergeBigFile("D:/test/1480745113027.cfg");
    }
}
