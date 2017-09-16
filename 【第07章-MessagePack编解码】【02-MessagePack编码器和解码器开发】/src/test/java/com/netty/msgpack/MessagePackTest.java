package com.netty.msgpack;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: 王俊超
 * Date: 2017-09-16 15:28
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class MessagePackTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessagePackTest.class);

    /**
     * 测试编解码
     *
     * @throws Exception
     */
    @Test
    public void testMessagePack_decodeAndEncode() throws Exception {
        MessageInfo src = new MessageInfo(1, "message", 1.1);
        LOGGER.info(src.toString());
        MessagePack msgpack = new MessagePack();
        // Serialize
        byte[] bytes = msgpack.write(src);
        // Deserialize
        MessageInfo dst = msgpack.read(bytes, MessageInfo.class);
        LOGGER.info(dst.toString());
        JSONAssert.assertEquals(src.toString(), dst.toString(), false);
    }


    @Test
    public void testMessagePack_multiInstance() throws Exception {
        final int number = 3;
        List<MessageInfo> src = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            src.add(new MessageInfo(i, "message" + i, i * 1.1));
        }

        MessagePack msgpack = new MessagePack();

        // 序列化
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = msgpack.createPacker(out);
        for (MessageInfo info : src) {
            packer.write(info);
        }
        byte[] bytes = out.toByteArray();

        // 反序列化
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Unpacker unpacker = msgpack.createUnpacker(in);
        List<MessageInfo> dst = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            dst.add(unpacker.read(MessageInfo.class));
        }

        LOGGER.info(src.toString());
        LOGGER.info(dst.toString());
        JSONAssert.assertEquals(src.toString(), dst.toString(), false);

    }


}
