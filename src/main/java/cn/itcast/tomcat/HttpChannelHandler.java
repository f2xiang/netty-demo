package cn.itcast.tomcat;

import cn.itcast.tomcat.http.Request;
import cn.itcast.tomcat.http.Response;
import cn.itcast.tomcat.servlet.BaseHttpServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;


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

        BaseHttpServlet baseHttpServlet = (BaseHttpServlet) MappingServletFactory.getService(req.getUrl());
        if (baseHttpServlet == null) {
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
