package com.netty.tcp.correct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * 此案例没有考虑读半包问题，一旦压力上来，或者发送大报文之后，就会存在粘包/拆包问题。如
 * 果代码没有考虑， 往往就会出现解码错位或者错误，导致程序不能正常工作。下面我们模拟故障
 * 场景。
 * 每读到一条消息后， 就计一次数，然后发送应答消息给客户端。按照设计，服务端接
 * 收到的消息总数应该跟客户端发迭的消息总数相同， 而且请求消息删除回车换行符后应该
 * 为”QUERY TIME ORDER”
 * <p>
 * Author: 王俊超
 * Date: 2017-09-08 07:48
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 类型转换，将msg 转换成Netty 的ByteBuf 对象。ByteBuf 类似于JDK 中的
        // java.nio.ByteBuffer 对象，不过它提供了更加强大和灵前的功能。通过ByteBuf 的
        // readableBytes 方法可以获取缓冲区可读的字节数，根据可读的字节数创建byte 数组，通过
        // ByteBuf 的readBytes 方法将缓冲区中的字节数组复制到新建的byte 数组中，最后通过new
        // String 构造函数获取请求消息。这时对请求消息进行判断， 如果是”QUERY TIME ORDER”
        // 则创建应答消息，通过ChannelHandlerContext writeAndFlush方法异步发送应答消息给客户端。
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        String body = new String(req, "UTF-8")
                .substring(0, req.length - System.getProperty("line.separator").length());
        System.out.println("The time server receive order : " + body
                + " ; the counter is : " + (++counter));
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                ? new java.util.Date(System.currentTimeMillis()).toString()
                : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        // 将消息发送队列中的消息写入到SocketChannel中发送给对方。从性能角度考虑， 为了防止频繁
        // 地唤醒Selector 进行消息发迭， Netty 的write 方法并不直接将消息写入SocketChannel 中，
        // 调用write 方法只是把待发迭的消息放到发送缓冲数组中， 再通过调用flush 方法，将发送
        // 缓冲区中的消息全部写到SocketChannel 中。
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
