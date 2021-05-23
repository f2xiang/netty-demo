package cn.itcast.handler;

import cn.itcast.message.RpcRequestMessage;
import cn.itcast.message.RpcResponseMessage;
import cn.itcast.server.service.HelloServiceImpl;
import cn.itcast.server.service.ServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 20:30
 */
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage>  {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestMessage message) throws Exception {
//        RpcRequestMessage message = new RpcRequestMessage(
//                "cn.itcast.server.service.HelloService",
//                "sayHello",
//                String.class,
//                new Class[] {String.class},
//                new Object[] {"张三"}
//        );

        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        try {
            Object service = ServiceFactory.getService(message.getInterfaceName());
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParamType());
            Object res = method.invoke(service, message.getParamVal());
            rpcResponseMessage.setRes(res);
        } catch (Exception e) {
            rpcResponseMessage.setException(new Exception("远程调用出错: cause by" + e.getCause().getMessage()));
        }

        System.out.println(rpcResponseMessage);
        channelHandlerContext.writeAndFlush(rpcResponseMessage);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

//        while (true) {
//            new Thread()
//        }
    }
}
