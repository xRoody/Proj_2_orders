package com.example.orders.validators;

import com.example.orders.DTOs.StatusDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;

import java.util.List;

public interface StatusValidator {
    List<BodyExceptionWrapper> validateNewStatus(StatusDTO statusDTO);
    List<BodyExceptionWrapper> validateUpdatedStatus(StatusDTO statusDTO);
}
