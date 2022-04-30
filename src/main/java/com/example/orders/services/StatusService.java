package com.example.orders.services;

import com.example.orders.DTOs.StatusDTO;
import com.example.orders.entityes.Status;

import java.util.List;

public interface StatusService {
    void addNewStatus(String value);
    boolean delete(String value);
    void update(StatusDTO s);
    Status getStatus(Long id);
    List<StatusDTO> getAllStatusesValues();
    boolean isExists(String value);
    StatusDTO getStatusValue(Long id);
    boolean delete(Long id);
    Status get(String value);
}
