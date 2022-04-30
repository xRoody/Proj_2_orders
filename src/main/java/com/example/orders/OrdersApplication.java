package com.example.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class OrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    @Bean(name = "8080")
    public WebClient webClient1(){
        return WebClient.create("http://localhost:8080");
    }
    @Bean(name = "8081")
    public WebClient webClient2(){
        return WebClient.create("http://localhost:8081");
    }
}
