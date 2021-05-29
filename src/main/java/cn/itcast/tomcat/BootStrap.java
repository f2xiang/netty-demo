package cn.itcast.tomcat;

import cn.itcast.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: Tomcat启动类
 * @author: fengx
 * @create: 2021-05-29 12:05
 */
@Slf4j
public class BootStrap {

    /**
     * 初始化
     */
    public void init() {
        openServer();
    }

    /**
     * 启动http netty 服务器
     */
    private void openServer() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 请求解码器
                    ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    // 将HTTP消息的多个部分合成一条完整的HTTP消息
                    ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                    // 响应转码器
                    ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    ch.pipeline().addLast("http-handler", new HttpChannelHandler());
                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        BootStrap bootStrap = new BootStrap();
        bootStrap.init();
    }


}
