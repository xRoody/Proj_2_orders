package com.example.orders.controllers;

import com.example.orders.DTOs.ChangedCharacteristicDTO;
import com.example.orders.DTOs.CharacteristicDTO;
import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.ChangedCharacteristicService;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.validators.OfferOrderCardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/card")
@Slf4j
public class OfferOrderCardController {
    private final OfferOrderCardService offerOrderCardService;
    private final OfferOrderCardValidator offerOrderCardValidator;
    private final ChangedCharacteristicService changedService;

    @GetMapping
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(offerOrderCardService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        OfferOrderCardDTO dto=offerOrderCardService.getDTO(id);
        if (dto==null){
            log.info("card not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        if (offerOrderCardService.delete(id)) return ResponseEntity.ok().build();
        log.info("card not deleted");
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/countByOfferId/{id}")
    public ResponseEntity<Object> count(@PathVariable("id") Long offerId){
        return ResponseEntity.ok(offerOrderCardService.countWithOfferId(offerId));
    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestBody OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=offerOrderCardValidator.validateNewOfferOrderCard(offerOrderCardDTO);
        if (reports.size()!=0) {
            log.info("bad request {}", reports);
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        offerOrderCardService.add(offerOrderCardDTO);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id,@RequestBody OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=offerOrderCardValidator.validateExistsOfferOrderCard(offerOrderCardDTO);
        if (reports.size()!=0) {
            log.info("bad request {}", reports);
            return new ResponseEntity<>(reports, HttpStatus.CONFLICT);
        }
        offerOrderCardDTO.setId(id);
        offerOrderCardService.update(offerOrderCardDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}/getInfo")
    public ResponseEntity<Object> getInfo(@PathVariable("id") Long id){
        List<CharacteristicDTO> characteristicDTOS=offerOrderCardService.getInfo(id);
        if (characteristicDTOS==null) {
            log.info("card not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(characteristicDTOS);
    }

    @PostMapping("/addChange")
    public ResponseEntity<Object> saveChangedCharacteristic(@RequestBody ChangedCharacteristicDTO characteristicDTO){
        changedService.save(characteristicDTO);
        return ResponseEntity.ok().build();
    }
}
