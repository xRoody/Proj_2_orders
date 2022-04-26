package com.example.orders.repositories;

import com.example.orders.entityes.OfferOrderCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferOrderCardRepo extends JpaRepository<OfferOrderCard, Long> {
}
