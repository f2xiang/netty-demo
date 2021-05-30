package cn.itcast.tomcat;

import cn.itcast.tomcat.http.Request;
import cn.itcast.tomcat.http.Response;
import cn.itcast.tomcat.servlet.BaseHttpServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * @description: Http请求处理
 * @author: fengx
 * @create: 2021-05-29 12:59
 */
@Slf4j
public class HttpChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        Request req = new Request(request);
        if ("/favicon.ico".equals(request.uri())) {
            return;
        }
        log.info("-----------进入{}请求, {},--------------{}",req.getHttpMethod().name(),req.getUrl(),req.getParam());
        //方式一：通过配置类，进行映射配置
        BaseHttpServlet baseHttpServlet = (BaseHttpServlet) MappingServletFactory.getService(req.getUrl());
        if (baseHttpServlet == null) {
            //方式二：通过controller类，存放到controller映射map
            Method method = BootStrap.getMethodMappingMap().get(req.getUrl());
            if (method == null) {
                // 两种方式都没找到 就是没有对应的处理类
                return;
            }

            // 取出要执行方法的所有的参数
            Object o = BootStrap.getMethodControllerMap().get(method.getName());
            Parameter[] parameters = method.getParameters();
            // 取出request里的值
            Object[] param = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                param[i] = req.getParam().get(parameters[i].getName());
            }
            // 执行方法
            Object invokeRes = method.invoke(o, param);
            write(ctx,new Response<>(invokeRes.toString()));
            return;
        }

        Response res = baseHttpServlet.service(req);
        write(ctx,res);

    }




    private void write(ChannelHandlerContext ctx, Response res) {
        //回写数据
        ctx.writeAndFlush(res.getResponse());
    }




}
