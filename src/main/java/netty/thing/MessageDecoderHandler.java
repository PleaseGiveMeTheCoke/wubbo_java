package netty.thing;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoderHandler extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] req = new byte[in.readableBytes()];
        in.readBytes(req);
        String s = new String(req,"UTF-8");
        Message message = JSONObject.parseObject(s,Message.class);
        out.add(message);
    }
}
