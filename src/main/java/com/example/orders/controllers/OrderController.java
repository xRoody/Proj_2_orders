package com.example.orders.controllers;

import com.example.orders.DTOs.OrderDTO;
import com.example.orders.DTOs.OrderWrapper;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OrderService;
import com.example.orders.validators.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderValidator orderValidator;

    private final SimpMessageSendingOperations operations;


    @GetMapping
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(orderService.getAll());
    }
    @GetMapping("/all")
    public ResponseEntity<Object> trueGetAll(){
        return ResponseEntity.ok(orderService.trueGetAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        OrderDTO orderDTO=orderService.getDTO(id);
        if (orderDTO==null){
            log.info("order not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        if(orderService.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable("id") Long id, @RequestBody Long statusId){
        if (!orderService.isExists(id)){
            log.info("order not found");
            return ResponseEntity.notFound().build();
        }
        orderService.changeStatus(id, statusId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody OrderWrapper orderDTO){
        List<BodyExceptionWrapper> reports=orderValidator.validateOrder(orderDTO.getOrder());
        if (reports.size()!=0) {
            log.info("bad request {}", reports);
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        Long id=orderService.add(orderDTO);
        operations.convertAndSend("/topic/new", orderService.getDTO(id));//here
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id,@RequestBody OrderDTO orderDTO){
        if (!orderService.isExists(orderDTO.getId())) return ResponseEntity.notFound().build();
        List<BodyExceptionWrapper> reports=orderValidator.validateOrder(orderDTO);
        if (reports.size()!=0) {
            log.info("bad request {}", reports);
            return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        }
        orderDTO.setId(id);
        orderService.update(orderDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/getPrice")
    public ResponseEntity<Object> getPrice(@PathVariable("id") Long id){
        Double d=orderService.getPrice(id);
        if (d==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(d);
    }
}
