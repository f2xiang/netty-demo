package cn.itcast.handler;

import cn.itcast.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 20:30
 */
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponseMessage rpcResponseMessage) throws Exception {
        System.out.println("rpc接受到的请求："+rpcResponseMessage);

        Promise<Object> promise = PROMISES.remove(rpcResponseMessage.getSequenceId());
        if (promise != null) {
            if (rpcResponseMessage != null && rpcResponseMessage.getException() == null) {
                promise.setSuccess(rpcResponseMessage.getRes());
            }else {
                promise.setFailure(rpcResponseMessage.getException());
            }
        }else {
            promise.setFailure(new Exception("no promise error"));
        }

    }
}
