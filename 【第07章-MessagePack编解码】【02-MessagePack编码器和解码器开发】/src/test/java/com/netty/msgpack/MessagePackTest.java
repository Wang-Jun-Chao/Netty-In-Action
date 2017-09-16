package com.netty.msgpack;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 序列化
        byte[] bytes = msgpack.write(src);
        // 反序列化
        MessageInfo dst = msgpack.read(bytes, MessageInfo.class);
        LOGGER.info(dst.toString());
        JSONAssert.assertEquals(src.toString(), dst.toString(), false);
    }


    /**
     * 序列化多个Java对象
     *
     * @throws Exception
     */
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

    /**
     * 多种数据类型（包括基本类型和基本类型的封装类型）的序列化和反序列化
     *
     * @throws Exception
     */
    @Test
    public void testMessagePack_multiDataType() throws Exception {
        MessagePack msgpack = new MessagePack();

        // 序列化
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = msgpack.createPacker(out);

        // 序列化基本类型
        boolean trueValue = true;
        int intValue = 10;
        double doubleValue = 10.5;
        packer.write(trueValue);
        packer.write(intValue);
        packer.write(doubleValue);

        // 序列化基本类型的包装类
        boolean trueWrapValue = Boolean.TRUE;
        Integer intWrapValue = new Integer(10);
        Double doubleWrapValue = new Double(10.5);
        packer.write(trueWrapValue);
        packer.write(intWrapValue);
        packer.write(doubleWrapValue);

        // 序列化数组
        int[] intArray = new int[]{1, 2, 3, 4};
        Double[] doubleWrapArray = new Double[]{10.5, 20.5};
        String[] stringArray = new String[]{"msg", "pack", "for", "java"};
        byte[] byteArray = new byte[]{0x30, 0x31, 0x32};
        packer.write(intArray);
        packer.write(doubleWrapArray);
        packer.write(stringArray);
        packer.write(byteArray);

        // 序列化其他引用类型
        String string = "MesagePack";
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0x30, 0x31, 0x32});
        BigInteger bigInteger = BigInteger.ONE;
        packer.write(string);
        packer.write(byteBuffer);
        packer.write(bigInteger);

        // 反序列化
        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Unpacker unpacker = msgpack.createUnpacker(in);

        // 基本类型
        boolean b = unpacker.readBoolean(); // boolean value
        LOGGER.info(trueValue + "");
        LOGGER.info(b + "");
        Assert.assertEquals(trueValue, b);

        int i = unpacker.readInt(); // int value
        LOGGER.info(intValue + "");
        LOGGER.info(i + "");
        Assert.assertEquals(intValue, i);

        double d = unpacker.readDouble(); // double value
        LOGGER.info(doubleValue + "");
        LOGGER.info(d + "");
        Assert.assertEquals(doubleValue, d, 0);

        // 基本类型的包装类
        Boolean wb = unpacker.read(Boolean.class);
        LOGGER.info(trueWrapValue + "");
        LOGGER.info(wb + "");
        Assert.assertEquals(trueWrapValue, wb);

        Integer wi = unpacker.read(Integer.class);
        LOGGER.info(intWrapValue + "");
        LOGGER.info(wi + "");
        Assert.assertEquals(intWrapValue, wi);

        Double wd = unpacker.read(Double.class);
        LOGGER.info(doubleWrapValue + "");
        LOGGER.info(wd + "");
        Assert.assertEquals(doubleWrapValue, wd);


        // 数组
        int[] ia = unpacker.read(int[].class);
        LOGGER.info(new Gson().toJson(intArray));
        LOGGER.info(new Gson().toJson(ia));
        Assert.assertArrayEquals(intArray, ia);

        Double[] da = unpacker.read(Double[].class);
        LOGGER.info(new Gson().toJson(doubleWrapArray));
        LOGGER.info(new Gson().toJson(da));
        Assert.assertArrayEquals(doubleWrapArray, da);

        String[] sa = unpacker.read(String[].class);
        LOGGER.info(new Gson().toJson(stringArray));
        LOGGER.info(new Gson().toJson(sa));
        Assert.assertArrayEquals(stringArray, sa);

        byte[] ba = unpacker.read(byte[].class);
        LOGGER.info(new Gson().toJson(byteArray));
        LOGGER.info(new Gson().toJson(ba));
        Assert.assertArrayEquals(byteArray, ba);

        //
        String ws = unpacker.read(String.class);
        LOGGER.info(new Gson().toJson(string));
        LOGGER.info(new Gson().toJson(ws));
        Assert.assertEquals(string, ws);

        ByteBuffer buf = unpacker.read(ByteBuffer.class);
        LOGGER.info(new Gson().toJson(byteBuffer));
        LOGGER.info(new Gson().toJson(buf));
        Assert.assertEquals(byteBuffer, buf);

        BigInteger bi = unpacker.read(BigInteger.class);
        LOGGER.info(new Gson().toJson(bigInteger));
        LOGGER.info(new Gson().toJson(bi));
        Assert.assertEquals(bigInteger, bi);
    }

    /**
     * List和Map类型的序列化和反序列化
     *
     * @throws Exception
     */
    @Test
    public void testMessagePack_ListAndMap() throws Exception {
        MessagePack msgpack = new MessagePack();

        // 创建List和Map序列化和反序列化的模板
        Template<List<String>> listTmpl = Templates.tList(Templates.TString);
        Template<Map<String, String>> mapTmpl = Templates.tMap(Templates.TString, Templates.TString);

        // 序列化
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = msgpack.createPacker(out);

        // 序列化List对象
        List<String> srcList = new ArrayList<String>();
        srcList.add("msgpack");
        srcList.add("for");
        srcList.add("java");
        packer.write(srcList); // List object

        // 序列化Map对象
        Map<String, String> srcMap = new HashMap<String, String>();
        srcMap.put("sadayuki", "furuhashi");
        srcMap.put("muga", "nishizawa");
        packer.write(srcMap); // Map object

        // 反序列化
        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Unpacker unpacker = msgpack.createUnpacker(in);


        List<String> dstList = unpacker.read(listTmpl);
        String s1 = new Gson().toJson(srcList);
        String s2 = new Gson().toJson(dstList);
        LOGGER.info(s1);
        LOGGER.info(s2);
        JSONAssert.assertEquals(s1, s2, false);


        Map<String, String> dstMap = unpacker.read(mapTmpl);
        s1 = new Gson().toJson(srcMap);
        s2 = new Gson().toJson(dstMap);
        LOGGER.info(s1);
        LOGGER.info(s2);
        JSONAssert.assertEquals(s1, s2, false);
    }

    /**
     * 不使用注解@Message进行序列化对象
     *
     * @throws Exception
     */
    @Test(expected = MessageTypeException.class)
    public void testMessagePack_withoutMessageAnnotation() throws Exception {
        MessageInfo2 src = new MessageInfo2(1, "message", 1.1);
        LOGGER.info(src.toString());
        MessagePack msgpack = new MessagePack();

        // 序列化
        // MessageInfo2上没有加@Mesasge注解，
        // 也没有使用msgpack.register(MessageInfo2.class);
        // 抛出MessageTypeException
        byte[] bytes = msgpack.write(src);
        // 反序列化
        MessageInfo2 dst = msgpack.read(bytes, MessageInfo2.class);
        LOGGER.info(dst.toString());
        JSONAssert.assertEquals(src.toString(), dst.toString(), false);
    }

    /**
     * 使用@Optional注解标注新添加的字段
     *
     * @throws Exception
     */
    @Test
    public void testMessagePack_withOptional() throws Exception {
        MessageInfo3 src = new MessageInfo3(1, "message", 1.1, 2);
        LOGGER.info(src.toString());
        MessagePack msgpack = new MessagePack();
        // 序列化
        byte[] bytes = msgpack.write(src);
        // 反序列化
        // 使用原有的版本反序列化，会忽略optional注解的字段
        MessageInfo dst = msgpack.read(bytes, MessageInfo.class);
        LOGGER.info(dst.toString());
    }

    /**
     * 动态的确定序列化的类型，主要是使用类库提供的Value对象
     *
     * @throws Exception
     */
    @Test
    public void testMessagePack_dynamic() throws Exception {
        // 创建序列化对象
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");

        MessagePack msgpack = new MessagePack();
        // 序列化
        byte[] raw = msgpack.write(src);

        // 直接使用模板序列化
        List<String> dst1 = msgpack.read(raw, Templates.tList(Templates.TString));
        String s1 = new Gson().toJson(src);
        String s2 = new Gson().toJson(dst1);
        LOGGER.info(s1);
        LOGGER.info(s2);
        JSONAssert.assertEquals(s1, s2, false);


        // 反序列化，然后进行类型转换
        Value dynamic = msgpack.read(raw);
        // TODO 如何确定的？
        LOGGER.info(dynamic.isArrayValue() + "");
        LOGGER.info(dynamic.asArrayValue().get(0).getClass() + "");
        List<String> dst2 = new Converter(dynamic).read(Templates.tList(Templates.TString));
        s1 = new Gson().toJson(src);
        s2 = new Gson().toJson(dst2);
        LOGGER.info(s1);
        LOGGER.info(s2);
        JSONAssert.assertEquals(s1, s2, false);
    }
}
