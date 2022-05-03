package com.example.orders.DTOs;

import lombok.Data;

@Data
public class CharacteristicDTO {
    private Long id;
    private String title;
    private Long cardId;
    private Integer quantity;
    private Double price;
    private Boolean isDurable;
}
