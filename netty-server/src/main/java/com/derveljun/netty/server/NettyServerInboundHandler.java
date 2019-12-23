package com.derveljun.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@ChannelHandler.Sharable
@Component
@Slf4j
public class NettyServerInboundHandler extends ChannelInboundHandlerAdapter {

    private StringBuffer messageGatheringBuffer;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        messageGatheringBuffer = new StringBuffer();
        log.info(" >> channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info(" >> channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info(" >> channelRead");
        messageGatheringBuffer.append(msg);
        ReferenceCountUtil.release(msg);

        //super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        log.info(" >> channelReadComplete Started");
        if (!StringUtils.isEmpty(messageGatheringBuffer)) {
            log.info(" >> channelReadCompleted, Received Message : " + messageGatheringBuffer.toString());
            messageGatheringBuffer = null;
            ctx.writeAndFlush("Data Received");
            ctx.close();
        } else {
            log.info(" >> channelReadCompleted, Received Message : Null");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error(cause.getMessage());
    }
}
