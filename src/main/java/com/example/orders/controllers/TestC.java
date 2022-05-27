package com.example.orders.controllers;

import com.example.orders.DTOs.OrderDTO;
import com.example.orders.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TestC {
    @GetMapping
    public String test(){
        return "KitchenPage";
    }

    @SendTo("/topic/new")
    @MessageMapping("/websocket")
    public OrderDTO getNew(@Payload OrderDTO orderDTO){
        return orderDTO;
    }

}
