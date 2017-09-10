package com.netty.tcp.correct;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Author: 王俊超
 * Date: 2017-09-08 07:39
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
public class TimeServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new TimeServer().bind(port);
    }

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        // 行创建了两个NioEventLoopG roup 实例。
        // NioEventLoopGroup 是个线和组， 它包含了一组NIO 线程， 专门用于网络事件的处理， 实
        // 际上它们就是Reactor 线程组。这里创建两个的原因是一个用于服务端接受客户端的连接，
        // 另一个用于进行SocketChannel 的网络读写。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Netty用于启动NIO 服务端的辅助启动类， 目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            // group 方法， 将两个NIO 线程组当作入参传递到ServerBootstrap 中。接
            // 着设置创建的Channel 为NioServerSocketChannel ， 它的功能对应于JDK NIO 类库中的
            // ServerSocketChannel 类。然后配置NioServerSocketChannel的TCP 参数，此处将它的backlog
            // 设置为1024，最后绑定IO事件的处理类ChildChannelHandler，它的作用类似于Reactor
            // 模式中的Handler 类， 主要用于处理网络IO 事件，例如记录日志、对消息进行编解码等。
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            // 绑定端口，同步等待成功
            // bind 方法绑定监听端口，随后，调用它
            // 的同步阻塞方法sync 等待绑定操作完成。完成之后Netty 会运回一个ChannelFuture ， 它
            // 的功能类似于JDK 的java.util.concurrent.Future ， 主要用于异步操作的通知回调。
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            // 等待服务端链路关闭之后main函数才退出。
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
