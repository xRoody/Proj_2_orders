package com.example.orders;

import com.example.orders.entityes.Other;
import com.example.orders.repositories.OtherRepo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class OrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
        /*otherRepo.save(new Other("AdminService",encoder.encode("Sup3r+S3cret-p4ssw0rd"),"ADMIN"));
        otherRepo.save(new Other("KitchenService",encoder.encode("Sup3r+S3cret-p4ssw0rd"),"KITCHEN"));
        otherRepo.save(new Other("UserService",encoder.encode("Sup3r+S3cret-p4ssw0rd"),"USER"));*/
    }

    @Bean(name = "8080")
    public WebClient webClient1(){
        return WebClient.create("http://localhost:8080");
    }
    @Bean(name = "8081")
    public WebClient webClient2(){
        return WebClient.create("http://localhost:8081");
    }
    @Bean
    public Module javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));//
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return module;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
}
