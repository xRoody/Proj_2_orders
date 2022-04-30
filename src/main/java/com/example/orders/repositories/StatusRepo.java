package com.example.orders.repositories;

import com.example.orders.entityes.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepo extends JpaRepository<Status, Long> {
    boolean existsByValue(String value);
    void deleteByValue(String value);
    Status getStatusByValue(String val);
}
