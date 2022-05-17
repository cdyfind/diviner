package com.diviner.netty.server;


import com.diviner.netty.domain.FileBurstInstruct;
import com.diviner.netty.domain.FileDescInfo;
import com.diviner.netty.domain.FileTransferProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import sun.awt.geom.AreaOp;


/**
 * 文件上传处理
 * ChannelInboundHandlerAdapter  in 数据进入时监听事件
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *当客户主动连接服务端的链接后，这个通到就是活跃的了。也就是客户端与服务端建立了通信通到并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        SocketChannel channel = (SocketChannel)ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端连接到本服务器。channelId："+channel.id());
        System.out.println("链接报告IP："+channel.localAddress().getHostString());
        System.out.println("链接报告Port："+channel.localAddress().getPort());
        System.out.println("链接报告完毕");
    }
    /**
     *  当客户端断开服务端的连接后，这个通到就是不活跃的，也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("客户端断开链接"+ctx.channel().localAddress().toString());
    }

    /**
     * 断点续传逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        //数据格式验证
        if(!(msg instanceof FileTransferProtocol)){
            return;
        }
        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        //0传输文件‘请求’、1文件传输‘指令’、2文件传输数据
        switch (fileTransferProtocol.getTransferType()){
            case 0:
                FileDescInfo fileDescInfo = (FileDescInfo)fileTransferProtocol.getTransferObj();

                //断点续传信息，实际应用中需要将断点续传保存到数据库中
//                FileBurstInstruct fileBurstInstruct =
        }
    }

}
