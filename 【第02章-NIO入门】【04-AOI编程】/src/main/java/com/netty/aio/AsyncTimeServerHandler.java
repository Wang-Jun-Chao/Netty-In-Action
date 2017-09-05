package com.netty.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Author: 王俊超
 * Date: 2017-09-05 07:52
 * All Rights Reserved !!!
 */
public class AsyncTimeServerHandler implements Runnable {
    private int port;
    private CountDownLatch latch;
    private AsynchronousServerSocketChannel channel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            channel = AsynchronousServerSocketChannel.open();
            channel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AsynchronousServerSocketChannel getChannel() {
        return channel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        channel.accept(this, new AcceptCompletionHandler());
    }
}
