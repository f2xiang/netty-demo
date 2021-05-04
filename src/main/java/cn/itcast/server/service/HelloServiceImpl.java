package cn.itcast.server.service;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 20:39
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好："+ name;
    }

}
