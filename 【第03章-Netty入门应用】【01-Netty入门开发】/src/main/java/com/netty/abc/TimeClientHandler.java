package com.netty.abc;

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
    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端和服务端TCP 链路建立成功之后， Netty 的NIO 线程会调用channelActive 方法，发送查
        // 询时间的指令给服务端，调用Cbanne!HandlerContext 的writeAndFlush 方法将请求消息发送给服务端。
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 当服务端返回应答消息时， channelRead 方法被调用，从Netty的ByteBuf中读取并打印应答消息。
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 释放资源
        // 当发生异常时，打印异常日志， 释放客户端资源。
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}
