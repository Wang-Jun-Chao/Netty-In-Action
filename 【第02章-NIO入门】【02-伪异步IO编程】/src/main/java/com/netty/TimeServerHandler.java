package com.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Author: 王俊超
 * Date: 2017-08-27 21:51
 * All Rights Reserved !!!
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream());
            String currentTime;
            String body;

            while (true) {
                // 通过Bu fferedReader 读取一行，如果已经读到了输入流的尾部，则返回值为
                // null ， 退出循环。如果读到了非空值， 则对内容进行判断， 如果请求消息为查询时间的指
                // 令"QUERY TIME ORDER"，则获取当前最新的系统时间，通过PrintWriter 的println 函数
                // 发送给客户端，最后退出循环
                body = in.readLine();
                if (body == null) {
                    break;
                }
                System.out.println("The time server receive order : " + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                        new Date(System.currentTimeMillis()).toString() : "BAD ORDER";

                out.print(currentTime);
            }

        } catch (IOException e) {

            // 放输入流、输出流和Socket 套接字句柄资源；段后线程自动销毁并被虚拟机回收。
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (out != null) {
                out.close();
            }

            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                this.socket = null;
            }
        }
    }
}
