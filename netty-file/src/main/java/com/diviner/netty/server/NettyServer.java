package com.diviner.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.java.Log;

/**
  * @Author caody
  * @Date 2022/4/29 15:08
  * @Param
  * @return {@link }
  **/
@Log
public class NettyServer {

    //配置服务器NIO线程组
     private EventLoopGroup bossGroup = new NioEventLoopGroup();
     private EventLoopGroup workerGroup = new NioEventLoopGroup();

     private Channel channel;

     public ChannelFuture bing(int port){
           ChannelFuture channelFuture = null;
           try {
               ServerBootstrap b = new ServerBootstrap();
               b.group(bossGroup,workerGroup)
                       .channel(NioServerSocketChannel.class)
                       .option(ChannelOption.SO_BACKLOG,128);
//                       .childHandler()
                channelFuture = b.bind(port).syncUninterruptibly();
                this.channel = channelFuture.channel();
           } catch (Exception e){
               e.printStackTrace();
           } finally {
               if (null != channelFuture && channelFuture.isSuccess()){
                   log.info("netty-file server start done");
               } else {
                    log.info("netty-file server start error");
               }
           }

           return channelFuture;
     }

     public void destory(){
         if (null == channel){
             return;
         }
         channel.close();
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
     }

     public Channel getChannel(){
         return channel;
     }
}
