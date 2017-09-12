package com.netty.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Author: 王俊超
 * Date: 2017-09-12 07:33
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class EchoClient {


    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new EchoClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) throws InterruptedException {
        // 配置客户端NIO线程组
        // 创建客户端处理IO读写的NioEventLoopGroup 线程组，然后继续创建客户端
        // 辅助启动类Bootstrap ， 随后需要对其进行配置。
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // Channel 需要设置为NioSocketChannel，然后为其添加Handler。此处
            // 为了简单直接创建匿名内部类， 实现initChannel 方法，其作用是当创建NioSocketChannel
            // 成功之后，在进行初始化时，将它的ChannelHandler 设置到ChannelPipeline 中，用于处理
            // 网络IO事件。
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            // 新增加了两个解码器，使用$_作为分割符
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            // 第一个1024表示单条消息的最大长度， 当达剑该长度后仍然没有查找到分隔符，就抛出
                            // TooLongFrameException 异常，防止由于异常码流缺失分隔符导致的内存溢出，
                            // 这是Netty 解码器的可靠性保护： 第二个参数就是分隔符缓冲对象。
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 发起异步连接操作
            // 调用conn ect 方法发起异步连接， 然后调用同步方法等待连接成功。
            ChannelFuture f = b.connect(host, port).sync();

            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
