package com.derveljun.netty.client;

import io.netty.util.CharsetUtil;

public class NettyClientApplication {

    public static void main(String[] args) throws InterruptedException {
        NettyClientBoot boot = new NettyClientBoot();
        String data = boot.start("0.0.0.0", 5900, "testdatasend".getBytes(CharsetUtil.UTF_8), 10);
        System.out.println(data);
    }
}
