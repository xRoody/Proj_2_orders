package com.example.orders.DTOs;

import lombok.Data;

@Data
public class ChangedCharacteristicDTO {
    private Long id;
    private Long cardId;
    private Integer quantity;
    private Double price;
}
