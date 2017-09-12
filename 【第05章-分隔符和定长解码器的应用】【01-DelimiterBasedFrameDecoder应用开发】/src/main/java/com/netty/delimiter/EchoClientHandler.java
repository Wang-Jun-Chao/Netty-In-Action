package com.netty.delimiter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author: 王俊超
 * Date: 2017-09-12 07:39
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class EchoClientHandler extends ChannelHandlerAdapter {
    private static final String ECHO_REQ = "Hi, Wang Junchao. Welcome to Netty.$_";
    private int counter;

    public EchoClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 在TCP 链路建立成功之后循环发送请求消息给服务端
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // 打印接收到的服务端应答消息同时进行计数。
        System.out.println("This is " + (++counter) + " times receive server : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
