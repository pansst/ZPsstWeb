package com.psst.common.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.psst.common.log4j.Log4jUtil;
import com.psst.common.util.PropertiesLoader;

public class FTPUtil {
    private static final int DEFAULT_FTP_PORT=21;
    private ThreadLocal<FTPClient> ftpClientThreadLocal = new ThreadLocal<FTPClient>();
    /** ftp设置参数  * */
    private FtpParam param;
    private static FTPUtil instance;
    static{
        instance= new FTPUtil();
        PropertiesLoader pLoader = new PropertiesLoader("ftp.properties");
        instance.setParam(new FtpParam(pLoader));
    }
    public FtpParam getParam() {
        return param;
    }
    /**
     * 返回一个FTPClient实例
     * 
     * @throws FTPClientException
     */
    private FTPClient getFTPClient() throws FTPClientException {
        if (ftpClientThreadLocal.get() != null && ftpClientThreadLocal.get().isConnected()) {
            return ftpClientThreadLocal.get();
        } else {
            FTPClient ftpClient = new FTPClient(); // 构造一个FtpClient实例
            ftpClient.setControlEncoding(param.getLanguage()); // 设置字符集

            connect(ftpClient); // 连接到ftp服务器

            // 设置为passive模式
            if (param.getMode() == null || param.getMode() == 0) {
                ftpClient.enterLocalPassiveMode();
            }
            setFileType(ftpClient); // 设置文件传输类型

            try {
                ftpClient.setSoTimeout(param.getTimeout() == null ? 0 : param.getTimeout());
            } catch (SocketException e) {
                 throw new FTPClientException("Set timeout error.", e);
            }
            ftpClientThreadLocal.set(ftpClient);
            return ftpClient;
        }
    }
    /**
     * 设置文件传输类型
     * 
     * @throws FTPClientException
     * @throws IOException
     */
    private void setFileType(FTPClient ftpClient) throws FTPClientException {
        try {
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            throw new FTPClientException("Could not to set file type.", e);
        }
    }
    
    /**
     * 连接到ftp服务器
     * 
     * @param ftpClient
     * @return 连接成功返回true，否则返回false
     * @throws FTPClientException
     */
    private boolean connect(FTPClient ftpClient) throws FTPClientException {
        try {
            ftpClient.connect(param.getHost(), param.getPort() == null || param.getPort() <= 0 ? DEFAULT_FTP_PORT : param.getPort());

            // 连接后检测返回码来校验连接是否成功
            int reply = ftpClient.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
                // 登陆到ftp服务器
                if (ftpClient.login(param.getUid(), param.getPassword())) {
                    setFileType(ftpClient);
                    return true;
                }
            } else {
                ftpClient.disconnect();
                throw new FTPClientException("FTP server refused connection.");
            }
        } catch (IOException e) {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect(); // 断开连接
                } catch (IOException e1) {
                    throw new FTPClientException("Could not disconnect from server.", e1);
                }
            }
            throw new FTPClientException("Could not connect to server.", e);
        }
        return false;
    }
    /**
     * 断开ftp连接
     * 
     * @throws FTPClientException
     */
    public void disconnect() throws FTPClientException {
        try {
            FTPClient ftpClient = getFTPClient();
            ftpClient.logout();
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
                ftpClient = null;
            }
        } catch (IOException e) {
            throw new FTPClientException("Could not disconnect from server.", e);
        }
    }

    public boolean mkdir(String pathname) throws FTPClientException {
        return mkdir(pathname, null);
    }

    /**
     * 在ftp服务器端创建目录（不支持一次创建多级目录）
     * 该方法执行完后将自动关闭当前连接
     * @param pathname
     * @return
     * @throws FTPClientException
     */
    public boolean mkdir(String pathname, String workingDirectory) throws FTPClientException {
        return mkdir(pathname, workingDirectory, true);
    }
    /**
     * 在ftp服务器端创建目录（不支持一次创建多级目录）
     * @param pathname
     * @param autoClose
     *            是否自动关闭当前连接
     * @return
     * @throws FTPClientException
     */
    public boolean mkdir(String pathname, String workingDirectory, boolean autoClose) throws FTPClientException {
        try {
            getFTPClient().changeWorkingDirectory(workingDirectory);
            return getFTPClient().makeDirectory(pathname);
        } catch (IOException e) {
            throw new FTPClientException("Could not mkdir.", e);
        } finally {
            if (autoClose) {
                disconnect(); // 断开连接
            }
        }
    }
    
    /**
     * 上传一个本地文件到远程指定文件
     * @param remoteAbsoluteFile
     *            远程文件名(包括完整路径)
     * @param localAbsoluteFile
     *            本地文件名(包括完整路径)
     * @return 成功时，返回true，失败返回false
     * @throws FTPClientException
     */
    public boolean put(String remoteAbsoluteFile, String localAbsoluteFile) throws FTPClientException {
        return put(remoteAbsoluteFile, localAbsoluteFile, true);
    }
    
    /**
     * 上传一个本地文件到远程指定文件
     * @param remoteAbsoluteFile
     *            远程文件名(包括完整路径)
     * @param localAbsoluteFile
     *            本地文件名(包括完整路径)
     * @param autoClose
     *            是否自动关闭当前连接
     * @return 成功时，返回true，失败返回false
     * @throws FTPClientException
     */
    public boolean put(String remoteAbsoluteFile, String localAbsoluteFile, boolean autoClose) throws FTPClientException {
        InputStream input = null;
        try {
            // 处理传输
            input = new FileInputStream(localAbsoluteFile);
            getFTPClient().storeFile(remoteAbsoluteFile, input);
            Log4jUtil.debug("put " + localAbsoluteFile);
            return true;
        } catch (FileNotFoundException e) {
            throw new FTPClientException("local file not found.", e);
        } catch (IOException e) {
            throw new FTPClientException("Could not put file to server.", e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                throw new FTPClientException("Couldn't close FileInputStream.", e);
            }
            if (autoClose) {
                disconnect(); // 断开连接
            }
        }
    }

    /**
     * 下载一个远程文件到本地的指定文件
     * @param remoteAbsoluteFile
     *            远程文件名(包括完整路径)
     * @param localAbsoluteFile
     *            本地文件名(包括完整路径)
     * @return 成功时，返回true，失败返回false
     * @throws FTPClientException
     */
    public boolean get(String remoteAbsoluteFile, String localAbsoluteFile) throws FTPClientException {
        return get(remoteAbsoluteFile, localAbsoluteFile, true);
    }

    /**
     * 下载一个远程文件到本地的指定文件
     * 
     * @param remoteAbsoluteFile
     *            远程文件名(包括完整路径)
     * @param localAbsoluteFile
     *            本地文件名(包括完整路径)
     * @param autoClose
     *            是否自动关闭当前连接
     * 
     * @return 成功时，返回true，失败返回false
     * @throws FTPClientException
     */
    public boolean get(String remoteAbsoluteFile, String localAbsoluteFile, boolean autoClose) throws FTPClientException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(localAbsoluteFile);
            return get(remoteAbsoluteFile, output, autoClose);
        } catch (FileNotFoundException e) {
            throw new FTPClientException("local file not found.", e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                throw new FTPClientException("Couldn't close FileOutputStream.", e);
            }
        }
    }
    
    /**
     * 下载一个远程文件到指定的流 处理完后记得关闭流
     * 
     * @param remoteAbsoluteFile
     * @param output
     * @return
     * @throws FTPClientException
     */
    public boolean get(String remoteAbsoluteFile, OutputStream output) throws FTPClientException {
        return get(remoteAbsoluteFile, output, true);
    }
    
    /**
     * 下载一个远程文件到指定的流 处理完后记得关闭流
     * 
     * @param remoteAbsoluteFile
     * @param output
     * @param delFile
     * @return
     * @throws FTPClientException
     */
    public boolean get(String remoteAbsoluteFile, OutputStream output, boolean autoClose) throws FTPClientException {
        try {
            FTPClient ftpClient = getFTPClient();
            // 处理传输
            return ftpClient.retrieveFile(remoteAbsoluteFile, output);
        } catch (IOException e) {
            throw new FTPClientException("Couldn't get file from server.", e);
        } finally {
            if (autoClose) {
                disconnect(); // 关闭链接
            }
        }
    }
    
    /**
     * 从ftp服务器上删除一个文件 该方法将自动关闭当前连接
     * 
     * @param delFile
     * @return
     * @throws FTPClientException
     */
    public boolean delete(String delFile) throws FTPClientException {
        return delete(delFile, true);
    }

    /**
     * 从ftp服务器上删除一个文件
     * @param delFile
     * @param autoClose
     *            是否自动关闭当前连接
     * @return
     * @throws FTPClientException
     */
    public boolean delete(String delFile, boolean autoClose) throws FTPClientException {
        try {
            getFTPClient().deleteFile(delFile);
            return true;
        } catch (IOException e) {
            throw new FTPClientException("Couldn't delete file from server.", e);
        } finally {
            if (autoClose) {
                disconnect(); // 关闭链接
            }
        }
    }

    /**
     * 批量删除 该方法将自动关闭当前连接
     * 
     * @param delFiles
     * @return
     * @throws FTPClientException
     */
    public boolean delete(String[] delFiles) throws FTPClientException {
        return delete(delFiles, true);
    }
    
    /**
     * 批量删除
     * @param delFiles
     * @param autoClose
     *            是否自动关闭当前连接
     * @return
     * @throws FTPClientException
     */
    public boolean delete(String[] delFiles, boolean autoClose) throws FTPClientException {
        try {
            FTPClient ftpClient = getFTPClient();
            for (String s : delFiles) {
                ftpClient.deleteFile(s);
            }
            return true;
        } catch (IOException e) {
            throw new FTPClientException("Couldn't delete file from server.", e);
        } finally {
            if (autoClose) {
                disconnect(); // 关闭链接
            }
        }
    }

    /**
     * 列出远程默认目录下所有的文件
     * 
     * @return 远程默认目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
     * @throws FTPClientException
     */
    public String[] listNames() throws FTPClientException {
        return listNames(null, true);
    }

    public String[] listNames(boolean autoClose) throws FTPClientException {
        return listNames(null, autoClose);
    }

    /**
     * 列出远程目录下所有的文件
     * 
     * @param remotePath
     *            远程目录名
     * @param autoClose
     *            是否自动关闭当前连接
     * 
     * @return 远程目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
     * @throws FTPClientException
     */
    public String[] listNames(String remotePath, boolean autoClose) throws FTPClientException {
        try {
            String path = getParam().getRootPath() + remotePath != null ? remotePath : "";
            String[] listNames = getFTPClient().listNames(path);
            return listNames;
        } catch (IOException e) {
            throw new FTPClientException("列出远程目录下所有的文件时出现异常", e);
        } finally {
            if (autoClose) {
                disconnect(); // 关闭链接
            }
        }
    }
    public static FTPUtil getInstance() {
        return instance;
    }

    public void setParam(FtpParam param) {
        this.param = param;
    }
    
    public static boolean downloadFile(String localFilePath, String ftpFilePath, boolean connectFlag) {
        try {
            FTPUtil ftp = getInstance();
            boolean flag = ftp.get(ftp.getParam().getRootPath() + ftpFilePath, localFilePath, connectFlag);
            return flag;
        } catch (Exception e) {
            Log4jUtil.error(e);
        }
        return false;
    }
    
    public static boolean uploadFile(String localFilePath, String ftpFilePath, boolean connectFlag) {
        try {
            FTPUtil ftp = getInstance();
            boolean flag = ftp.put(ftp.getParam().getRootPath() + ftpFilePath, localFilePath, connectFlag);
            return flag;
        } catch (Exception e) {
            Log4jUtil.error(e);
        }
        return false;
    }
    public static void main(String[] args) {
        try {
            long beginTime = System.currentTimeMillis();
            FTPUtil ftp = getInstance();
            String path = "/voice-source";
            String localPath = "E:/systest";
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
}
