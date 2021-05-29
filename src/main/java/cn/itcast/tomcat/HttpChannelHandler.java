package cn.itcast.tomcat;

import cn.itcast.tomcat.http.Request;
import cn.itcast.tomcat.servlet.BaseHttpServlet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import io.netty.util.CharsetUtil;
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

        String res = baseHttpServlet.service(req);
        write(ctx,res);

    }




    private void write(ChannelHandlerContext ctx, String res) {
        //给客户端回传数据
        ByteBuf byteBuf = Unpooled.copiedBuffer(res, CharsetUtil.UTF_8);
        //使用DefaultFullHttpResponse作为response
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        //设置头信息,以及编码，防止乱码
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=utf8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, byteBuf.readableBytes());

        //回写数据
        ctx.writeAndFlush(response);
    }




}
