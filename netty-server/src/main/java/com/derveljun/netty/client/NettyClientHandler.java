package com.derveljun.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private byte[] reqMessage;
    private String resMessage;
    private ByteBuf recvMsgBuf;

    public NettyClientHandler(byte[] reqMessage) {
        this.reqMessage = reqMessage;
        this.recvMsgBuf = ByteBufAllocator.DEFAULT.buffer();
    }

    public String readResponseMessage(){
        recvMsgBuf.release();
        recvMsgBuf = null;
        return resMessage;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        recvMsgBuf.writeBytes(byteBuf.toString(CharsetUtil.UTF_8).getBytes(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer(reqMessage));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        this.resMessage = recvMsgBuf.toString(CharsetUtil.UTF_8);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error(cause.getMessage());
        ctx.close();
    }
}
