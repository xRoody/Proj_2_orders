package com.example.orders.DTOs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderWrapper {
    private OrderDTO order;
    private List<ChangedCharacteristicDTO> changes=new ArrayList<>();
}
