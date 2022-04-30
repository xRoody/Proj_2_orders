package com.example.orders.validators;

import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;

import java.util.List;

public interface OfferOrderCardValidator {
    List<BodyExceptionWrapper> validateNewOfferOrderCard(OfferOrderCardDTO offerOrderCardDTO);
    List<BodyExceptionWrapper> validateExistsOfferOrderCard(OfferOrderCardDTO offerOrderCardDTO);
    List<BodyExceptionWrapper> validateOfferOrderCardFromOrderValidator(OfferOrderCardDTO offerOrderCardDTO);
}
