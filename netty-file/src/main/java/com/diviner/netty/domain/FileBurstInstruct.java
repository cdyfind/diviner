package com.diviner.netty.domain;

/**
 * 文件分片指令
 * @Author caody
 * @Date 2022/5/10 11:27
 * @Param
 * @return {@link }
 **/
public class FileBurstInstruct {

    private Integer status;                //Constants.fileStatus  (0开始、1中间、2结尾、3完成)
    private String clientFileUrl;          //客户端文件的URL
    private Integer readPosition;          //读取位置

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public Integer getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(Integer readPosition) {
        this.readPosition = readPosition;
    }
}
