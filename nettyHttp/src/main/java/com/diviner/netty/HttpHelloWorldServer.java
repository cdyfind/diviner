package com.diviner.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelloWorldServer {
    private static  final Logger logger = LoggerFactory.getLogger(HttpHelloWorldServer.class);

    static final int PORT = 8888;
    public static void main(String[] args) throws Exception {
        //1个端口用一个监听
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            //设置两个队列的最大数，主要影响accept取出的连接
            b.option(ChannelOption.SO_BACKLOG,1024);
            //禁用nagle算法，nagle是为了尽可能发送大块数据，避免小数据块
            b.childOption(ChannelOption.TCP_NODELAY,true);
            //监控连接是否有效，如果处于空闲2个小时会发送一个数据包给远程的socket，如果没有相应，tcp会持续尝试11分钟，12分钟还没有响应，会关闭连接
            b.childOption(ChannelOption.SO_KEEPALIVE,true);
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpHelloworldServerInitalizer());
            Channel ch = b.bind(PORT).sync().channel();
            logger.info("Netty http sever listening on port"+PORT);
            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
