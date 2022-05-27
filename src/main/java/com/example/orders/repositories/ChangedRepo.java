package com.example.orders.repositories;

import com.example.orders.EmbededIds.ChangedId;
import com.example.orders.entityes.ChangedCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangedRepo extends JpaRepository<ChangedCharacteristic, ChangedId> {
    long countAllByChangedId_CardId(Long cardId);
}
