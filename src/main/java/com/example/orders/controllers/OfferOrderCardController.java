package com.example.orders.controllers;

import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.validators.OfferOrderCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/card")
public class OfferOrderCardController {
    private final OfferOrderCardService offerOrderCardService;
    private final OfferOrderCardValidator offerOrderCardValidator;

    @GetMapping
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(offerOrderCardService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        OfferOrderCardDTO dto=offerOrderCardService.getDTO(id);
        if (dto==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        if (offerOrderCardService.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/countByOfferId/{id}")
    public ResponseEntity<Object> count(@PathVariable("id") Long offerId){
        return ResponseEntity.ok(offerOrderCardService.countWithOfferId(offerId));
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=offerOrderCardValidator.validateNewOfferOrderCard(offerOrderCardDTO);
        if (reports.size()!=0) return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        offerOrderCardService.add(offerOrderCardDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=offerOrderCardValidator.validateExistsOfferOrderCard(offerOrderCardDTO);
        if (reports.size()!=0) return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        offerOrderCardService.update(offerOrderCardDTO);
        return ResponseEntity.ok().build();
    }
}
