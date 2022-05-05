package com.example.orders.controllers;

import com.example.orders.DTOs.OrderDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OrderService;
import com.example.orders.validators.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN')")
    @GetMapping
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        OrderDTO orderDTO=orderService.getDTO(id);
        if (orderDTO==null){
            log.info("order not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        if(orderService.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable("id") Long id, @RequestBody Long statusId){
        if (!orderService.isExists(id)){
            log.info("order not found");
            return ResponseEntity.notFound().build();
        }
        orderService.changeStatus(id, statusId);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER', 'KITCHEN')")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody OrderDTO orderDTO){
        List<BodyExceptionWrapper> reports=orderValidator.validateOrder(orderDTO);
        if (reports.size()!=0) {
            log.info("bad request {}", reports);
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        orderService.add(orderDTO);
        return ResponseEntity.created(URI.create("/offers")).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN')")
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
    @PreAuthorize("hasAnyRole('ADMIN','USER', 'KITCHEN')")
    @GetMapping("/{id}/getPrice")
    public ResponseEntity<Object> getPrice(@PathVariable("id") Long id){
        Double d=orderService.getPrice(id);
        if (d==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(d);
    }
}
