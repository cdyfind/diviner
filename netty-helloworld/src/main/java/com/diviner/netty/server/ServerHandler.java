package com.diviner.netty.server;

import io.netty.channel.*;

import java.net.Inet4Address;
import java.util.Date;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 建立链接时发送一条信息
     * @Author caody
     * @Date 2022/4/15 17:18
     * @Param
     * @return {@link }
     **/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ctx.write("Welcome to" + Inet4Address.getLoopbackAddress().getHostName()+"!\r\n");
        ctx.write("It is"+  new Date() +"now.\r\n");
        ctx.flush();
    }

    /**
     * 业务逻辑处理
     * @Author caody
     * @Date 2022/4/15 17:19
     * @Param
     * @return {@link }
     **/
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        //Generate and write a response.
        String response;
        boolean close = false;
        if (request.isEmpty()){
            response = "Please type something.\r\n";
        } else if ("bye".equals(request.toLowerCase())){
            response = "Have a good day!\r\n";
            close = true;
        } else {
            response = "Did you say '"+request+"'?\r\n";
        }

        ChannelFuture future = ctx.write(response);
        if (close){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 数据读取完后调用方法
     * @Author caody
     * @Date 2022/4/15 17:59
     * @Param
     * @return {@link }
     **/
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    /**
     * 异常处理
     * @Author caody
     * @Date 2022/4/15 18:00
     * @Param
     * @return {@link }
     **/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
