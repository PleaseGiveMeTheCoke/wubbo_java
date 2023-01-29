package netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Invocation;
import constant.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.thing.Message;
import netty.thing.MessageDecoderHandler;
import netty.thing.MessageEncoderHandler;
import org.aopalliance.intercept.MethodInvocation;
import provider.Exporter;
import provider.Invoker;


public class Server {
    private volatile static Server server = null;
    public static void openServer(int port){
        if(server == null){
            synchronized (Server.class){
                if(server == null){
                    server = new Server(port);
                }
            }
        }
    }

    private Server(int port){
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MessageEncoderHandler());
                        ch.pipeline().addLast(new MessageDecoderHandler());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                Message request = (Message) msg;
                                if(request.getType() == Constants.REQUEST_TYPE){
                                    JSONObject obj = (JSONObject) request.getRes();
                                    Invocation invocation = JSONObject.parseObject(obj.toJSONString(), Invocation.class);
                                    Invoker invoker = Exporter.getInvoker(invocation.getInterfaceName());
                                    Object res;
                                    if(invoker == null){
                                        res = null;
                                    }else{
                                        res = invoker.invoke(invocation);
                                    }
                                    ctx.writeAndFlush(new Message(request.getId(),Constants.RESPONSE_TYPE,res));
                                }
                            }
                        });
                    }
                })
                .bind(port);
    }
}
