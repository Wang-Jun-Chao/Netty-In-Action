package com.netty.tcp.correct;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Author: 王俊超
 * Date: 2017-09-08 07:59
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new TimeClient().connect(port, "127.0.0.1");
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
            // 成功之后，在进行初始化时，将它的C hannelHa ndler 设置到ChannelPipeline 中，用于处理
            // 网络IO事件。
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
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
