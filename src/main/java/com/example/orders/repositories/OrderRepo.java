package com.example.orders.repositories;

import com.example.orders.entityes.Order;
import com.example.orders.entityes.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
    long countByStatus(Status status);
}
