package com.netty;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: 王俊超
 * Date: 2017-08-27 21:30
 * All Rights Reserved !!!
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        // TimeServer 根据传入的参数设置监听端口，如果没有入参，使用默认值8080
        int port = 8080;
        if (args != null && args.length > 0) {

            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                // 使用默认值
            }
        }

        ServerSocket server = null;
        try {
            // 如果端口合法且没有被占用，服务端监听成功
            server = new ServerSocket(port);
            System.out.println("The time server is start in port: " + port);
            Socket socket;
            // 通过一个无限循环来监昕客户端的连接，如果没有客户端接入，
            // 则主线程阻塞在 ServerSocket 的accept 操作上。打印线程堆拢，我们可
            // 以发现主程序确实阻塞在accept 操作上
            while (true) {
                socket = server.accept();
                // 当有新的客户端接入的时候，执行代码第34 行，以Socket 为
                // 参数构造TimeServerHandler 对象， TirneServerHandler
                // 是一个Runnable ， 使用它为构造函数的参数创建一个新的客户端
                // 线程处理这条Socket 链路。
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            if (server != null) {
                System.out.println("The time server closed");
                server.close();
            }
        }
    }
}
