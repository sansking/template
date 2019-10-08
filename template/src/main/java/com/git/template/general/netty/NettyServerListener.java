package com.git.template.general.netty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.git.template.general.netty.utils.ObjectCodec;

// Netty连接信息配置类
// 

/**
 * 服务启动监听器
 *
 * @author 叶云轩
 */
@Component
public class NettyServerListener {
    /**
     * NettyServerListener 日志输出器
     *
     * @author 叶云轩 create by 2017/10/31 18:05
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerListener.class);

    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup work = new NioEventLoopGroup();
    
    @Autowired
    private ServerChannelHandlerAdapter channelHandlerAdapter;
    @Autowired
    private NettyConfig nettyConfig;


    
    @PreDestroy
    public void close() {
        LOGGER.info("关闭服务器....");
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }
    
    public void start() {
        // 从配置文件中(application.yml)获取服务端监听端口号
        int port = nettyConfig.getPort();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO));
        try {
            //设置事件处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(NettyConstant.getMaxFrameLength()
                            , 0, 2, 0, 2));
                    pipeline.addLast(new LengthFieldPrepender(2));
                    pipeline.addLast(new ObjectCodec());

                    pipeline.addLast(channelHandlerAdapter);
                }
            });
            LOGGER.info("netty服务器在[{}]端口启动监听", port);
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.info("[出现异常] 释放资源");
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}