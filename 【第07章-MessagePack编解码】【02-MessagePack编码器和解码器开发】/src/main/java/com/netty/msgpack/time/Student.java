package com.netty.msgpack.time;

import org.msgpack.annotation.Message;

/**
 * Author: 王俊超
 * Date: 2017-09-16 08:37
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
@Message
public class Student {

    private int id;

    private String name;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + "]";
    }

}
