package com.example.orders.controllers;

import com.example.orders.DTOs.StatusDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OrderService;
import com.example.orders.services.StatusService;
import com.example.orders.validators.StatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statuses")
public class StatusController {
    private final StatusService statusService;
    private final StatusValidator statusValidator;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(statusService.getAllStatusesValues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        StatusDTO s=statusService.getStatusValue(id);
        if (s!=null) return ResponseEntity.ok(s);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> addNewStatus(@RequestBody StatusDTO statusDTO){
        List<BodyExceptionWrapper> reports=statusValidator.validateNewStatus(statusDTO);
        if (reports.size()!=0) return new ResponseEntity<>(reports,HttpStatus.BAD_REQUEST);
        statusService.addNewStatus(statusDTO.getValue());
        return ResponseEntity.created(URI.create("/statuses")).build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody StatusDTO statusDTO){
        if (!statusService.isExists(statusDTO.getValue())) return ResponseEntity.notFound().build();
        List<BodyExceptionWrapper> reports=statusValidator.validateUpdatedStatus(statusDTO);
        if (reports.size()!=0) return new ResponseEntity<>(reports,HttpStatus.CONFLICT);
        statusService.update(statusDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        if (orderService.countByStatus(id)!=0) return new ResponseEntity<>(new BodyExceptionWrapper("e-003", "Can't delete status with attachments"), HttpStatus.BAD_REQUEST);
        if (statusService.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }
}
