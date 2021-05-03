package cn.itcast.handler;

import cn.itcast.message.ChatRequestMessage;
import cn.itcast.message.ChatResponseMessage;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-01 23:13
 */
public class ChatResMessageHandler extends SimpleChannelInboundHandler<ChatResponseMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatResponseMessage chatResponseMessage) throws Exception {
        String from = chatResponseMessage.getFrom();
        String content = chatResponseMessage.getContent();

        System.out.println("收到来自"+from+"的消息：：："+content);


    }



}
