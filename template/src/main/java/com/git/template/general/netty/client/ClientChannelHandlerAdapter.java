package com.git.template.general.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.git.template.general.netty.msg.MethodInvokeMeta;

public class ClientChannelHandlerAdapter extends ChannelInboundHandlerAdapter{
    
	private Logger logger = LoggerFactory.getLogger(ClientChannelHandlerAdapter.class);
    private MethodInvokeMeta methodInvokeMeta;
    private CustomChannelInitializerClient channelInitializerClient;

    public ClientChannelHandlerAdapter(MethodInvokeMeta methodInvokeMeta, CustomChannelInitializerClient channelInitializerClient) {
        this.methodInvokeMeta = methodInvokeMeta;
        this.channelInitializerClient = channelInitializerClient;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("客户端出异常了,异常信息:{}", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (methodInvokeMeta.getMethodName().endsWith("toString") && !"class java.lang.String".equals(methodInvokeMeta.getReturnType().toString()))
            logger.info("客户端发送信息参数:{},信息返回值类型：{}", methodInvokeMeta.getArgs(), methodInvokeMeta.getReturnType());
        ctx.writeAndFlush(methodInvokeMeta);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelInitializerClient.setResponse(msg);
    }
}