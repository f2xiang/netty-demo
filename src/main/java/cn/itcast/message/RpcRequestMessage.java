package cn.itcast.message;

import cn.itcast.server.service.HelloServiceImpl;
import cn.itcast.server.service.ServiceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 20:27
 */
@Data
@AllArgsConstructor
public class RpcRequestMessage extends Message {

    private int sequenceId;
    private String interfaceName;
    private String methodName;
    private Class returnType;

    private Class[] paramType;
    private Object[] paramVal;


    @Override
    public int getMessageType() {
        return RpcRequestMessage;
    }


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RpcRequestMessage message = new RpcRequestMessage(
                1,
                "cn.itcast.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[] {String.class},
                new Object[] {"张三"}
        );

        HelloServiceImpl helloService = (HelloServiceImpl)ServiceFactory.getService(message.getInterfaceName());
        Method method = helloService.getClass().getMethod(message.getMethodName(), message.getParamType());
        Object res  = method.invoke(helloService, message.getParamVal());
        System.out.println(res);
    }


}
