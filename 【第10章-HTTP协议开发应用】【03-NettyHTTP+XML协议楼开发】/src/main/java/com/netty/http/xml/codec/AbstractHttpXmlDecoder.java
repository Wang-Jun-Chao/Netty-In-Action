package com.netty.http.xml.codec;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.StringReader;
import java.nio.charset.Charset;

public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);
    private StringReader reader;
    private boolean isPrint;
    private Class<?> clazz;
    private XmlMapper xmlMapper = new XmlMapper();

    protected AbstractHttpXmlDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    protected AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    protected Object decode0(ChannelHandlerContext arg0, ByteBuf body) throws Exception {

        String content = body.toString(UTF_8);
        if (isPrint) {
            System.out.println("The body is : " + content);
        }
        return xmlMapper.readValue(content, clazz);
    }
}
