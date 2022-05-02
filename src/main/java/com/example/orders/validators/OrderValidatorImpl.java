package com.example.orders.validators;

import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.services.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderValidatorImpl implements OrderValidator{
    private final OfferOrderCardService offerOrderCardService;
    private final OfferOrderCardValidator offerOrderCardValidator;
    private final StatusService statusService;
    private WebClient client8081;

    @Autowired
    @Qualifier(value = "8081")
    public void setClient8081(WebClient client) {
        this.client8081 = client;
    }

    public List<BodyExceptionWrapper> validateOrder(OrderDTO orderDTO){
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        if(orderDTO.getDob().isAfter(LocalDateTime.now())) reports.add(new BodyExceptionWrapper("t-003", "Incorrect date"));
        if (!statusService.isExists(orderDTO.getStatus())) reports.add(new BodyExceptionWrapper("e-003", "This status is not exists"));
        validateCards(new ArrayList<>(orderDTO.getOfferOrderCards()), reports);
        return reports;
    }

    private void validateCards(List<OfferOrderCardDTO> offerOrderCards, List<BodyExceptionWrapper> reports) {
        for (OfferOrderCardDTO dto:offerOrderCards){
            if (offerOrderCardService.isExists(dto.getId())){
                reports.addAll(offerOrderCardValidator.validateExistsOfferOrderCard(dto));
            }
            else reports.addAll(offerOrderCardValidator.validateOfferOrderCardFromOrderValidator(dto));
        }
    }
}
