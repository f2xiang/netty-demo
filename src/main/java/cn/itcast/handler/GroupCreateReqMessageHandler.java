package cn.itcast.handler;

import cn.itcast.message.GroupCreateRequestMessage;
import cn.itcast.message.GroupCreateResponseMessage;
import cn.itcast.server.session.Group;
import cn.itcast.server.session.GroupSessionFactory;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-03 16:17
 */
public class GroupCreateReqMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupCreateRequestMessage groupCreateRequestMessage) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().createGroup(groupCreateRequestMessage.getGroupName(), groupCreateRequestMessage.getMembers());
        channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(true, "创建成功"));

        List<Channel> membersChannel = GroupSessionFactory.getGroupSession().
                getMembersChannel(group.getName());
        for (Channel channel: membersChannel) {
            channel.writeAndFlush(
                    new GroupCreateResponseMessage(true, "您已经被拉入[" + group.getName() + "]"));
        }
    }



}
