package com.git.template.general.netty.practice;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

@Sharable
public class LoggingHandler extends ChannelDuplexHandler{
	 // 实际使用的日志处理，slf4j、log4j等
    protected final InternalLogger logger;
    // 日志框架使用的日志级别
    protected final InternalLogLevel internalLevel;
    // Netty使用的日志级别
    private final LogLevel level;
    
    // 默认级别为Debug
    private static final LogLevel DEFAULT_LEVEL = LogLevel.DEBUG;
    
    public LoggingHandler(LogLevel level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        // 获得实际的日志框架
        logger = InternalLoggerFactory.getInstance(getClass());
        // 设置日志级别
        this.level = level;
        internalLevel = level.toInternalLevel();
    }
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logMessage(ctx, "RECEIVED", msg);   // 记录日志
        ctx.fireChannelRead(msg);   // 传播事件
    }
    
    private void logMessage(ChannelHandlerContext ctx, String eventName, Object msg) {
        if (logger.isEnabled(internalLevel)) {
            logger.log(internalLevel, format(ctx, formatMessage(eventName, msg)));
        }
    }
    
    
    
    private String format(ChannelHandlerContext ctx, String formatMessage) {
		return "当前上下文:"+ctx +";当前信息:" + formatMessage;
	}

	protected String formatMessage(String eventName, Object msg) {
        if (msg instanceof ByteBuf) {
            return formatByteBuf(eventName, (ByteBuf) msg);
        } else if (msg instanceof ByteBufHolder) {
            return formatByteBufHolder(eventName, (ByteBufHolder) msg);
        } else {
            return formatNonByteBuf(eventName, msg);
        }
    }
    
    private String formatNonByteBuf(String eventName, Object msg) {
		ByteBuf b = ByteBufUtil.encodeString(ByteBufAllocator.DEFAULT, CharBuffer.wrap(msg.toString()), Charset.forName("utf-8"));
		return formatByteBuf(eventName, b);
	}

	private String formatByteBufHolder(String eventName, ByteBufHolder msg) {
		return formatByteBuf(eventName,msg.content());
	}

	protected String formatByteBuf(String eventName, ByteBuf msg) {
        int length = msg.readableBytes();
        if (length == 0) {
            StringBuilder buf = new StringBuilder(eventName.length() + 4);
            buf.append(eventName).append(": 0B");
            return buf.toString();
        } else {
            int rows = length / 16 + (length % 15 == 0? 0 : 1) + 4;
            StringBuilder buf = new StringBuilder(eventName.length() + 
                        2 + 10 + 1 + 2 + rows * 80);

            buf.append(eventName)
                      .append(": ").append(length).append('B').append(System.lineSeparator());
            ByteBufUtil.appendPrettyHexDump(buf, msg);

            return buf.toString();
        }
}
}