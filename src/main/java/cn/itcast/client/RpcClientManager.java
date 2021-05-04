package cn.itcast.client;

import cn.itcast.handler.RpcInvocationHandler;
import cn.itcast.handler.RpcRequestMessageHandler;
import cn.itcast.handler.RpcResponseMessageHandler;
import cn.itcast.message.RpcRequestMessage;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProcotolFrameDecoder;
import cn.itcast.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

import static cn.itcast.handler.RpcResponseMessageHandler.PROMISES;

@Slf4j
public class RpcClientManager {

    private static Channel channel;
    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcRequestMessageHandler RPC_REQUEST = new RpcRequestMessageHandler();
        RpcResponseMessageHandler RPC_RESPONSE = new RpcResponseMessageHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast(RPC_REQUEST);
                    ch.pipeline().addLast(RPC_RESPONSE);
                }
            });
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener((future) -> group.shutdownGracefully());
        } catch (Exception e) {
            log.error("client error", e);
        }
    }



    // 我们要把这种调用封装起来 通过 helloSrevice.sayHello这种正常人能看懂的方式，通过代理
   public static Object rpcInvoke() throws InterruptedException {
       return channel.writeAndFlush(   new RpcRequestMessage(
               1,
                    "cn.itcast.server.service.HelloService",
                            "sayHello",
                    String.class,
                    new Class[] {String.class},
            new Object[] {"张三"}
            ));
   }


    public static Object rpcInvoke(RpcRequestMessage rpcRequestMessage) throws Exception {
        // 如何获取返回值？
        // 这种直接获取 会返回null
//        channel.writeAndFlush( rpcRequestMessage ).getNow();

        // 发起方在main线程，发送调用是netty的nio线程
        // 也就是说 main 要等nio获取到数据以后，再把数据接过来
        // 利用promise， main 提供一个空书包，等nio线程把书包装满就继续运行
        DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
        PROMISES.put(rpcRequestMessage.getSequenceId(), promise);

        channel.writeAndFlush( rpcRequestMessage );
        promise.await();
        if (promise.isSuccess()) {
            return promise.get();
        }else {
            throw new Exception(promise.cause());
        }
    }

   public static <T> T getProxyService(Class<T> interfaceClass) {
       return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
               new Class[]{interfaceClass},
               new RpcInvocationHandler(interfaceClass.getName())
       );
   }

    public static void main(String[] args) throws InterruptedException {
//        Object res = rpcInvoke();
//        System.out.println(res);


        HelloService helloService = getProxyService(HelloService.class);

        System.out.println("response:"+helloService.sayHello("张三"));
        System.out.println("response:"+helloService.sayHello("王五"));
    }
}
