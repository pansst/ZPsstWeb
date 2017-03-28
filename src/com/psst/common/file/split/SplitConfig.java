package com.psst.common.file.split;

import java.io.Serializable;
import java.util.List;

/**
 * 文件拆分配置文件
 * @author shiyongsheng
 *
 */
public class SplitConfig implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String bigFileName;
    private Long bigFileSize;
    private List<String> littleFiles;
    public String getBigFileName() {
        return bigFileName;
    }
    public void setBigFileName(String bigFileName) {
        this.bigFileName = bigFileName;
    }
    public Long getBigFileSize() {
        return bigFileSize;
    }
    public void setBigFileSize(Long bigFileSize) {
        this.bigFileSize = bigFileSize;
    }
    public List<String> getLittleFiles() {
        return littleFiles;
    }
    public void setLittleFiles(List<String> littleFiles) {
        this.littleFiles = littleFiles;
    }
}
