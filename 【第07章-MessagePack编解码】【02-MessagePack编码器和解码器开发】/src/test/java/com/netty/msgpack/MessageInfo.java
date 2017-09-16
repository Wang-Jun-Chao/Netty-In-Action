package com.netty.msgpack;

import com.google.gson.Gson;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * 序列化的类要使用注解@Message
 * <p>
 * Author: 王俊超
 * Date: 2017-09-13 07:49
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
@Message
public class MessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double version;

    public MessageInfo() {
    }

    public MessageInfo(int id, String name, double version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

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

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
