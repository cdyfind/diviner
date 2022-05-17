package com.diviner.netty.codec;

import com.diviner.netty.domain.FileTransferProtocol;
import com.diviner.netty.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 文件解码
 * 把byte数组转换成对象
 */
public class ObjDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public ObjDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        //readableBytes 返回可读的字节数
        if (in.readableBytes() < 4){
            return;
        }
        //Mark   将当前的位置指针被分到mark变量中
        //Reset  恢复位置指针为mark中的变量
        // 将当前readerIndex备份到markedReaderIndex中
        in.markReaderIndex();
        //readerIndex 以Little Endian Byte顺序获取当前的32位浮点数，并在此缓冲区中增加readerIndex by 4。
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        list.add(SerializationUtil.deserialize(data,genericClass));

    }

    public static void main(String[] args) {

        ByteBuf buffer = Unpooled.buffer(60);
        System.out.println("1.创建一个非池化的ByteBuf，大小为14个字节");
        System.out.println("ByteBuf空间大小：" + buffer.capacity());
        // 2.写入3个字节
        buffer.writeByte(62);
        buffer.writeByte(75);
        buffer.writeByte(67);
        System.out.println("\r\n2.写入3个字节");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        System.out.println("readableBytes位置：" + buffer.readableBytes());

        // 3.写入一段字节
        byte[] bytes = {73, 74, 61, 63, 0x6B};
        buffer.writeBytes(bytes);
        System.out.println("\r\n3.写入一段字节");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());

        // 4.读取全部内容
//        byte[] allBytes = new byte[buffer.readableBytes()];
//        buffer.readBytes(allBytes);
//        System.out.println("\r\n4.读取全部内容");
//        System.out.println("readerIndex位置：" + buffer.readerIndex());
//        System.out.println("writerIndex位置：" + buffer.writerIndex());
//        System.out.println("读取全部内容：" + Arrays.toString(allBytes));
        int readableBytes = buffer.readableBytes();
        System.out.println("readableBytes位置：" + readableBytes);
        //readerIndex + 4 取32位浮点数
//        int readInt = buffer.readInt();
//        System.out.println("readInt位置：" + readInt);


    }
}
