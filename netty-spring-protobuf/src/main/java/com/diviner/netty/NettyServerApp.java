package com.diviner.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.diviner.netty.server"})
public class NettyServerApp {


    public static void main(String[] args) {
        SpringApplication.run(NettyServerApp.class);
    }
}
