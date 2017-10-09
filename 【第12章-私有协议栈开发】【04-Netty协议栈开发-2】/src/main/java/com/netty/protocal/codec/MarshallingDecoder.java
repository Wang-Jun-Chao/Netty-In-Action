package com.netty.protocal.codec;


import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Marshalling解码器，它能接收的对象的最大值是：1048576字节，如果接收到的对象的最大值大于1048576字节，
 * 就会抛出StreamCorruptedException异常
 */
public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception {
        // 取对象的二进制流的大小
        int objectSize = in.readInt();
        // 取对象的二制流的ByteBuf对象
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ByteInput input = new ChannelBufferByteInput(buf);
        try {
            // 读取对象
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();

            // 标记新的读取位置
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        } finally {
            unmarshaller.close();
        }
    }
}
