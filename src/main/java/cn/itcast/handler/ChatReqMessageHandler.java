package cn.itcast.handler;

import cn.itcast.message.ChatRequestMessage;
import cn.itcast.message.ChatResponseMessage;
import cn.itcast.server.service.UserServiceFactory;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.*;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-01 23:13
 */
public class ChatReqMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRequestMessage chatRequestMessage) throws Exception {
        String from = chatRequestMessage.getFrom();
        String to = chatRequestMessage.getTo();
        String content = chatRequestMessage.getContent();

        System.out.println(from+"__"+to+"--"+content);

        Channel toChannel = SessionFactory.getSession().getChannel(to);
        if (toChannel == null) {
            System.out.println("对方不在线");
            return;
        }
        ChatResponseMessage chatResponseMessage = new ChatResponseMessage(from, content);
        toChannel.writeAndFlush(chatResponseMessage);

    }



}
