package cn.itcast.tomcat.controller;

import cn.itcast.tomcat.annotation.RequestMapping;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-29 19:56
 */
@RequestMapping("/hello")
public class HelloController implements Controller {


    @RequestMapping("/say")
    public String sayHello(String name) {

        return name + "   say hello ~";
    }

}
