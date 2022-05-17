package com.diviner.netty.codec;

import com.diviner.netty.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 文件编码,
 * 把对象转换成byte数组
 */
public class ObjEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public ObjEncoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(in)){
            byte[] data = SerializationUtil.serialize(in);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
