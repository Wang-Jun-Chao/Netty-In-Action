package com.netty.msgpack;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Author: 王俊超
 * Date: 2017-09-13 07:49
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/Wang-Jun-Chao
 * All Rights Reserved !!!
 */
@Message
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private int userId;
    private int age;

    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo buildUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public UserInfo buildAge(int age) {
        this.userId = age;
        return this;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "{\"userName\":\"" + userName + "\", \"userId\":" + userId + ", \"age:\":" + age +
                '}';
    }
}
