package com.diviner.netty.domain;

/**
 * 文件描述信息
 * @Author caody
 * @Date 2022/5/10 11:22
 * @Param
 * @return {@link }
 **/
public class FileDescInfo {

    private String fileUrl;
    private String fileName;
    private Long fileSize;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
