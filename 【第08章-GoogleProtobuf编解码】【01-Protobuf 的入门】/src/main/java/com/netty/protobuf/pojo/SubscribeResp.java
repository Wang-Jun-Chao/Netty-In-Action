package com.netty.protobuf.pojo;

import java.io.Serializable;

/**
 * Author: 王俊超
 * Date: 2017-09-19 08:21
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class SubscribeResp implements Serializable {

    /**
     * 默认序列ID
     */
    private static final long serialVersionUID = 1L;

    private int subReqID;

    private int respCode;

    private String desc;

    public final int getSubReqID() {
        return subReqID;
    }

    public final void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public final int getRespCode() {
        return respCode;
    }

    public final void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public final String getDesc() {
        return desc;
    }

    public final void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeResp [subReqID=" + subReqID + ", respCode=" + respCode
                + ", desc=" + desc + "]";
    }
}
