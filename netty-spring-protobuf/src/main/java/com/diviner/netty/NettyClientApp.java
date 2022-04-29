package com.diviner.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.diviner.netty.client"})
public class NettyClientApp {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApp.class);
    }
}
