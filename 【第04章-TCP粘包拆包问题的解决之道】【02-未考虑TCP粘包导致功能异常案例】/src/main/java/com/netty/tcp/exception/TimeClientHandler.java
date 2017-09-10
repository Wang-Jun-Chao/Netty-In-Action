package com.netty.tcp.exception;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;


/**
 * Author: 王俊超
 * Date: 2017-09-08 07:48
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    private int counter = 0;
    private byte[] req;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator"))
                .getBytes();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端和服务端TCP 链路建立成功之后， Netty 的NIO 线程会调用channelActive 方法，发送查
        // 询时间的指令给服务端，调用ChannelHandlerContext 的writeAndFlush 方法将请求消息发送给服务端。
        // 客户端跟服务端链路建立成功之后，循环发送
        // 100 条消息，每发送一条就刷新一次，保证每条消息都会被写入Channel 中。按照我们的
        // 设计，服务端应该接收到l00 条查询时间指令的请求消息，事实并非如此。
        ByteBuf message = null;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 当服务端返回应答消息时， channelRead 方法被调用，从Netty的ByteBuf中读取并打印应答消息。
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        // 客户端每接收到服务端一条应答消息之后，就打印一次计数器。按照
        // 设计初衷，客户端应该打印10 0 次服务端的系统时间，事实并非如此
        System.out.println("Now is : " + body + " ; the counter is : " + (++counter));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 释放资源
        // 当发生异常时，打印异常日志， 释放客户端资源。
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}
