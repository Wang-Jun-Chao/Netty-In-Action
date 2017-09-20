package com.netty.protobuf.pojo;

import java.io.Serializable;

/**
 * Author: 王俊超
 * Date: 2017-09-19 08:20
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class SubscribeReq implements Serializable {

    /**
     * 默认的序列号ID
     */
    private static final long serialVersionUID = 1L;

    private int subReqID;

    private String userName;

    private String productName;

    private String phoneNumber;

    private String address;

    public final int getSubReqID() {
        return subReqID;
    }

    public final void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public final String getUserName() {
        return userName;
    }

    public final void setUserName(String userName) {
        this.userName = userName;
    }

    public final String getProductName() {
        return productName;
    }

    public final void setProductName(String productName) {
        this.productName = productName;
    }

    public final String getPhoneNumber() {
        return phoneNumber;
    }

    public final void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public final String getAddress() {
        return address;
    }

    public final void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SubscribeReq [subReqID=" + subReqID + ", userName=" + userName
                + ", productName=" + productName + ", phoneNumber="
                + phoneNumber + ", address=" + address + "]";
    }
}
