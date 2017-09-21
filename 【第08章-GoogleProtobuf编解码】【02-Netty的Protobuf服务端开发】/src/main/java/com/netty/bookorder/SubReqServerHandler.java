package com.netty.bookorder;

import com.netty.protobuf.proto.SubscribeReqProto;
import com.netty.protobuf.proto.SubscribeRespProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author: 王俊超
 * Date: 2017-09-20 22:14
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 由于ProtobutDecoder 已经对消息进行了自动解，码， 因此接收到的订购请求消息可以
        // 直接使用。对用户名进行校验， 校验通过后构造应答消息返回给客户端， 由于使用了
        // ProtobufEncoder ， 所以不需要对SubscribeRespProto.SubscribeResp进行手工编码。
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("Wangjunchao".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscribe req : [" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqId()));
        }
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqId) {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqId(subReqId);
        builder.setRespCode(0);
        builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
