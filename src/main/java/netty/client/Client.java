package netty.client;

import constant.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.thing.DefaultFuture;
import netty.thing.Message;
import netty.thing.MessageDecoderHandler;
import netty.thing.MessageEncoderHandler;

import java.net.InetSocketAddress;
import java.util.UUID;

public class Client {
    Channel channel;
    public Client(String host, int port){
        try {
            this.channel = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MessageDecoderHandler());
                            ch.pipeline().addLast(new MessageEncoderHandler());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    DefaultFuture.received(ctx.channel(),(Message) msg);
                                }
                            });
                            ch.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    super.write(ctx, msg, promise);
                                }
                            });
                        }
                    })
                    .connect(new InetSocketAddress(host, port))
                    .sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public DefaultFuture request(Object msg){
        Message request = new Message(UUID.randomUUID().toString(), Constants.REQUEST_TYPE,msg);
        DefaultFuture defaultFuture = new DefaultFuture(channel,request);
        channel.writeAndFlush(request);
        return defaultFuture;
    }
}
