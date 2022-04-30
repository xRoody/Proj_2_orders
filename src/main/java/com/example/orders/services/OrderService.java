package com.example.orders.services;

import com.example.orders.DTOs.CatAndPriceDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.entityes.Order;

import java.util.List;

public interface OrderService {
    long countByStatus(Long id);
    boolean isExists(Long id);
    Order get(Long id);
    OrderDTO getDTO(Long id);
    OrderDTO getDTOByObj(Order order);
    List<OrderDTO> getAll();
    void add(OrderDTO orderDTO);
    boolean delete(Long id);
    void update(OrderDTO orderDTO);
    void changeStatus(Long orderId, Long statusId);
    List<CatAndPriceDTO> getInfo(Long id);
}
