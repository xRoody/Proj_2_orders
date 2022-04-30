package com.example.orders.validators;

import com.example.orders.DTOs.OrderDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;

import java.util.List;

public interface OrderValidator {
    List<BodyExceptionWrapper> validateOrder(OrderDTO orderDTO);
}
