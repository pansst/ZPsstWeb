package com.psst.common.ftp;

import java.io.Serializable;

import com.psst.common.util.PropertiesLoader;

/**
 * ftp参数
 * 
 */
public class FtpParam implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** FTP Server所在操作系统类型：WINDOWS，UNIX，VMS，UNIX_LTRIM * */
	private String os;
	/** 服务器字符集:zh(中文),utf-8 * */
	private String language;
	/** ftp服务器ip地址 * */
	private String host;
	/** ftp端口号 * */
	private Integer port;
	/** ftp登录账号 * */
	private String uid;
	/** ftp登录密码 * */
	private String password;
	/** 模式:0为被动模式，1为主动模式  * */
	private Integer mode;
	/** 连接超时  * */
	private Integer timeout;
	/** ftp服务器日期格式 * */
	private String dateFormat;
	/** 源文件相对路径 * */
	private String sourceContextPath;
	/** 转码后相对路径 * */
	private String voiceContextPath;
	/** 转写后的转写文件所在路径 * */
	private String transContextPath;

	private String rootPath;
	private String targetPath;
	public FtpParam() {
        super();
    }
	public FtpParam(PropertiesLoader loader) {
        super();
        this.os = loader.getProperty("ftp.os");
        this.language = loader.getProperty("ftp.language");
        this.host = loader.getProperty("ftp.host");
        this.port = loader.getInteger("ftp.port");
        this.uid = loader.getProperty("ftp.uid");
        this.password = loader.getProperty("ftp.password");
        this.mode = loader.getInteger("ftp.mode");
        this.timeout = loader.getInteger("ftp.timeout");
        this.dateFormat = loader.getProperty("ftp.dateformat");
        this.rootPath = loader.getProperty("ftp.path.rootpath");
        
    }

    public String getRootPath() {
        return rootPath;
    }
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    public String getTargetPath() {
        return targetPath;
    }
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
    public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSourceContextPath() {
		return sourceContextPath;
	}

	public void setSourceContextPath(String sourceContextPath) {
		this.sourceContextPath = sourceContextPath;
	}

	public String getVoiceContextPath() {
		return voiceContextPath;
	}

	public void setVoiceContextPath(String voiceContextPath) {
		this.voiceContextPath = voiceContextPath;
	}

	public String getTransContextPath() {
		return transContextPath;
	}

	public void setTransContextPath(String transContextPath) {
		this.transContextPath = transContextPath;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
	public String getFullFtpVoicePath(){
		return "ftp://"+host+((port==null||port.intValue()==21)?"":":"+port)+(voiceContextPath.startsWith("/")?voiceContextPath:"/"+voiceContextPath);
	}
	
	public String getFullFtpTransPath(){
		return "ftp://"+host+((port==null||port.intValue()==21)?"":":"+port)+(transContextPath.startsWith("/")?transContextPath:"/"+transContextPath);
	}

}
