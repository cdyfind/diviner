package com.diviner.netty.util;

import com.diviner.netty.domain.Constants;
import com.diviner.netty.domain.FileBursData;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件读写工具
 */
public class FileUtil {

    public static FileBursData readFile(String fileUrl,Integer readPosition) throws IOException{
        File file = new File(fileUrl);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");//r: 只读模式 rw读写模式
        randomAccessFile.seek(readPosition);
        byte[] bytes = new byte[1024 * 100];
        int readSize = randomAccessFile.read(bytes);
        if (readSize <= 0){
            randomAccessFile.close();
            return new FileBursData(Constants.FileStatus.COMPLETE);
        }

        FileBursData fileInfo = new FileBursData();
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileName(file.getName());
        fileInfo.setBeginPos(readPosition);
        fileInfo.setEndPos(readPosition+readSize);
        //不足1024需要拷贝去掉空字节
        if (readSize < 1024*100){
            byte[] copy = new byte[readSize];
            System.arraycopy(bytes,0,copy,0,readSize);
            fileInfo.setBytes(copy);
            fileInfo.setStatus(Constants.FileStatus.END);
        } else {
            fileInfo.setBytes(bytes);
            fileInfo.setStatus(Constants.FileStatus.CENTER);
        }
        randomAccessFile.close();
        return fileInfo;
    }


}
