package com.netty.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author: 王俊超
 * Date: 2017-09-12 07:42
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // 直接将接收的消息打印出来， 由于DelimiterBasedFrameDecoder 自动对请
        // 求消息进行了解码，后续的ChannelHandler 接收到的msg 对象就是个完整的消息包。
        // 在EchoServer中，第二个ChannelHandler 是StringDecoder ，它将ByteBuf 解码成字符串对象：第三个
        // EchoServerHandler 接收到的msg 消息就是解码后的字符串对象。
        String body = (String) msg;
        System.out.println("This is " + (++counter) + " times receive client : [" + body + "]");

        // 由于我们设置DelimiterBasedFrameDecoder 过滤掉了分隔符，所以，返回给客户端
        // 时需要在请求消息尾部拼接分隔符“孔”， 最后创建Byte Buf， 将原始消息重新返回给客户端。
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
