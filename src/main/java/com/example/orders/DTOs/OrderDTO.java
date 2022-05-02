package com.example.orders.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDTO {
    private Long id;
    private String status;
    private LocalDateTime dob;
    private Collection<OfferOrderCardDTO> offerOrderCards=new HashSet<>();
}
