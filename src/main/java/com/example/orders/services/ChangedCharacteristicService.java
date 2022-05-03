package com.example.orders.services;

import com.example.orders.DTOs.ChangedCharacteristicDTO;
import com.example.orders.DTOs.CharacteristicDTO;

public interface ChangedCharacteristicService {
    double computeIfChanged(CharacteristicDTO characteristicDTO);

    void save(ChangedCharacteristicDTO characteristicDTO);
}
