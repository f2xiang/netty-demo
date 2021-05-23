package cn.itcast.handler;

import cn.itcast.message.LoginRequestMessage;
import cn.itcast.message.LoginResponseMessage;
import cn.itcast.server.service.UserServiceFactory;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-01 22:27
 */
public class ServerLoginHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestMessage loginRequestMessage) throws Exception {
        String username = loginRequestMessage.getUsername();
        String password = loginRequestMessage.getPassword();
        boolean success = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (success) {
            SessionFactory.getSession().bind(channelHandlerContext.channel(), username);
            message = new LoginResponseMessage(true, "登陆成功");
        }else {
            message = new LoginResponseMessage(false, "登陆失败");
        }
        System.out.println(message);
        channelHandlerContext.writeAndFlush(message);
    }
}
