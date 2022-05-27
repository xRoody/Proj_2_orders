package com.example.orders.repositories;

import com.example.orders.entityes.OfferOrderCard;
import com.example.orders.entityes.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OfferOrderCardRepo extends JpaRepository<OfferOrderCard, Long> {
    long countByOfferId(Long id);

    Collection<OfferOrderCard> findAllByThisOrder(Order order);
}
