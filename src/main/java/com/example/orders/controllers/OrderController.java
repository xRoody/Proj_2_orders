package com.example.orders.controllers;

import com.example.orders.DTOs.CatAndPriceDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OrderService;
import com.example.orders.validators.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderValidator orderValidator;

    @GetMapping
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        OrderDTO orderDTO=orderService.getDTO(id);
        if (orderDTO==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        if(orderService.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable("id") Long id, @RequestBody Long statusId){
        if (!orderService.isExists(id)) return ResponseEntity.notFound().build();
        orderService.changeStatus(id, statusId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody OrderDTO orderDTO){
        List<BodyExceptionWrapper> reports=orderValidator.validateOrder(orderDTO);
        if (reports.size()!=0) return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        orderService.add(orderDTO);
        return ResponseEntity.created(URI.create("/offers")).build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody OrderDTO orderDTO){
        if (!orderService.isExists(orderDTO.getId())) return ResponseEntity.notFound().build();
        List<BodyExceptionWrapper> reports=orderValidator.validateOrder(orderDTO);
        if (reports.size()!=0) return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        orderService.update(orderDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/getInfo")
    public ResponseEntity<Object> getInfo(@PathVariable("id") Long id) {
        List<CatAndPriceDTO> dto=orderService.getInfo(id);
        if (dto==null) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
