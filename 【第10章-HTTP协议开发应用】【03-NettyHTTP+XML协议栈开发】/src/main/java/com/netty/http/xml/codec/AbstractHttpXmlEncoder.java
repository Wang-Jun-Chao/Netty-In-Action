
package com.netty.http.xml.codec;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;

public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);
    private XmlMapper xmlMapper = new XmlMapper();

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws Exception {
        String xmlStr = xmlMapper.writeValueAsString(body);
        return Unpooled.copiedBuffer(xmlStr, UTF_8);
    }
}
