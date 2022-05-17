package com.diviner.netty.server;

import com.diviner.netty.codec.ObjDecoder;
import com.diviner.netty.codec.ObjEncoder;
import com.diviner.netty.domain.FileTransferProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        对象传输处理
        ch.pipeline().addLast(new ObjDecoder(FileTransferProtocol.class));
        ch.pipeline().addLast(new ObjEncoder(FileTransferProtocol.class));


    }
}
