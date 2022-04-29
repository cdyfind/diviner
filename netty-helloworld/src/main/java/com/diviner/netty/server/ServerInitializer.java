package com.diviner.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                //添加帧限定符来防止粘包现象
                .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                //解码和编码，应和客户端一致
                .addLast(new StringDecoder())
                .addLast(new StringEncoder())
                //处理请求
                .addLast(new ServerHandler());
    }
}
