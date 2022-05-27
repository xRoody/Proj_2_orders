package com.example.orders.services;

import com.example.orders.DTOs.CharacteristicDTO;
import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.entityes.OfferOrderCard;
import com.example.orders.entityes.Order;

import java.util.Collection;
import java.util.List;

public interface OfferOrderCardService {
    Long add(OfferOrderCardDTO offerOrderCardDTO);
    boolean delete(Long id);
    OfferOrderCard getObj(Long id);
    OfferOrderCardDTO getDTO(Long id);
    OfferOrderCardDTO getDTOByObj(OfferOrderCard offerOrderCard);
    List<OfferOrderCardDTO> getAll();
    void update(OfferOrderCardDTO offerOrderCardDTO);
    long countWithOfferId(Long id);
    boolean isExists(Long id);
    OfferOrderCard getObjByDTO(OfferOrderCardDTO offerOrderCardDTO);
    List<CharacteristicDTO> getInfo(Long id);

    Double getPrice(OfferOrderCardDTO dto);

    Collection<OfferOrderCard> findAllByOrder(Order order);
}
