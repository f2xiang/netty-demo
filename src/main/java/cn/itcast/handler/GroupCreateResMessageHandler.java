package cn.itcast.handler;

import cn.itcast.message.GroupCreateResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 16:25
 */
public class GroupCreateResMessageHandler extends SimpleChannelInboundHandler<GroupCreateResponseMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupCreateResponseMessage groupCreateResponseMessage) throws Exception {
        System.out.println("[创建群聊响应：]"+groupCreateResponseMessage.getReason());
    }
}
