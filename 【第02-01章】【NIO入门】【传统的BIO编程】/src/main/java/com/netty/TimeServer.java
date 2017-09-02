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
        int port  = 8080;
        if (args != null && args.length > 0) {

            try {
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException ex) {
                // 使用默认值
            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port: " + port);
            Socket socket;
            while (true) {
                socket = server.accept();
                new Thread( new TimeServerHandler(socket)).start();
            }
        } finally {
            if (server != null) {
                System.out.println("The time server closed");
                server.close();
            }
        }
    }
}
