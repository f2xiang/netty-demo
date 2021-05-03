package cn.itcast.handler;

import cn.itcast.message.ChatRequestMessage;
import cn.itcast.message.GroupCreateRequestMessage;
import cn.itcast.message.LoginRequestMessage;
import cn.itcast.message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: fengx
 * @create: 2021-05-01 22:08
 */
public class ClientLoginHandler extends SimpleChannelInboundHandler<LoginResponseMessage> {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private boolean loginSuccess = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入用户名");
            String username = scanner.nextLine();
            System.out.println("请输入密码");
            String psw = scanner.nextLine();
            ctx.writeAndFlush(new LoginRequestMessage(username, psw));
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 校验失败
            if (!loginSuccess) {
                ctx.channel().close();
                System.out.println("退出系统");
                return;
            }

            // 校验成功

            while (true) {
                System.out.println("输入命令 ");
                String command = scanner.nextLine();
                String[] s = command.split("_");

                switch (s[0]) {
                    case "send":
                        // 假设 lisi_你好
                        ChatRequestMessage chatRequestMessage = new ChatRequestMessage(username,s[1], s[2]);
                        ctx.writeAndFlush(chatRequestMessage);
                        break;

                    case "newgroup":
                        // 假设 lisi_你好
                        GroupCreateRequestMessage groupCreateRequestMessage = new GroupCreateRequestMessage(s[1], new HashSet<>(Arrays.asList(s[2].split(","))));
                        ctx.writeAndFlush(groupCreateRequestMessage);
                        break;

                }



            }

        }, "login-Thread").start();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponseMessage loginResponseMessage) throws Exception {
        System.out.println("客户端收到消息："+loginResponseMessage.getReason());
        loginSuccess = loginResponseMessage.isSuccess();
        countDownLatch.countDown();
    }
}
