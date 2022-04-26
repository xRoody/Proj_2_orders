package com.example.orders.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfferOrderCardDTO {
    private Long id;
    private Long offerId;
    private int quantity;
    private double price;
    private Long orderId;
}
