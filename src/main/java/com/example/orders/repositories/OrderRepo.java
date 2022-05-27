package com.example.orders.repositories;

import com.example.orders.entityes.Order;
import com.example.orders.entityes.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    long countByStatus(Status status);

    List<Order> findAllByStatusNotOrderByDob(Status status);

}
