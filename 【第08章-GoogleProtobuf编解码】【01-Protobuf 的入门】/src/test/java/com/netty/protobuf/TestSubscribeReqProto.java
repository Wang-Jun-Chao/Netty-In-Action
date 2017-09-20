package com.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.netty.protobuf.proto.SubscribeReqProto;

/**
 * Author: 王俊超
 * Date: 2017-09-20 07:38
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class TestSubscribeReqProto {
    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }
}
