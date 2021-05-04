package cn.itcast.handler;

import cn.itcast.client.RpcClientManager;
import cn.itcast.message.RpcRequestMessage;
import cn.itcast.util.SequenceIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-04 11:11
 */
public class RpcInvocationHandler implements InvocationHandler {

    private String interfaceClassName;

    public RpcInvocationHandler(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequestMessage message = new RpcRequestMessage(
                SequenceIdGenerator.nextId(),
                interfaceClassName,
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes(),
                args
        );

        return RpcClientManager.rpcInvoke(message);
    }
}
