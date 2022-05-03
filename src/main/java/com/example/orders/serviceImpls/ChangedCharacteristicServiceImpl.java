package com.example.orders.serviceImpls;

import com.example.orders.DTOs.ChangedCharacteristicDTO;
import com.example.orders.DTOs.CharacteristicDTO;
import com.example.orders.EmbededIds.ChangedId;
import com.example.orders.entityes.ChangedCharacteristic;
import com.example.orders.repositories.ChangedRepo;
import com.example.orders.repositories.OfferOrderCardRepo;
import com.example.orders.services.ChangedCharacteristicService;
import com.example.orders.services.OfferOrderCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ChangedCharacteristicServiceImpl implements ChangedCharacteristicService {
    private ChangedRepo changedRepo;
    @Override
    public double computeIfChanged(CharacteristicDTO characteristicDTO) {
        ChangedId changedId=new ChangedId(characteristicDTO.getId(), characteristicDTO.getCardId());
        double d=0;
        if (changedRepo.existsById(changedId)){
            ChangedCharacteristic c=changedRepo.getById(changedId);
            int q=characteristicDTO.getQuantity()+c.getQuantity();
            d=c.getQuantity()*characteristicDTO.getPrice();
            characteristicDTO.setQuantity(Math.max(q, 0));
        }
        return d;
    }

    @Override
    public void save(ChangedCharacteristicDTO characteristicDTO) {
        changedRepo.save(getObjByDTO(characteristicDTO));
    }

    private ChangedCharacteristic getObjByDTO(ChangedCharacteristicDTO characteristicDTO){
        return ChangedCharacteristic.builder()
                .changedId(ChangedId.builder().cardId(characteristicDTO.getCardId()).id(characteristicDTO.getId()).build())
                .quantity(characteristicDTO.getQuantity())
                .build();
    }
}
