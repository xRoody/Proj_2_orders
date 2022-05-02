package com.example.orders.controllers;

import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.exceptions.OfferNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({OfferNotFound.class})
    public ResponseEntity<Object> offerExceptionHandler(){
        return new ResponseEntity<>(new BodyExceptionWrapper("404", "Offer not found"), HttpStatus.NOT_FOUND);
    }
}
