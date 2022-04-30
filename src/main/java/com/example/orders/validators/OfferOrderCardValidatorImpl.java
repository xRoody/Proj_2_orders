package com.example.orders.validators;

import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OfferOrderCardValidatorImpl implements OfferOrderCardValidator{
    private final OrderService orderService;
    private final OfferOrderCardService offerOrderCardService;
    private  WebClient client;

    @Autowired
    @Qualifier(value = "8080")
    public void setClient(WebClient client) {
        this.client = client;
    }

    public List<BodyExceptionWrapper> validateNewOfferOrderCard(OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        if (offerOrderCardDTO.getQuantity()<=0) reports.add(new BodyExceptionWrapper("e-003", "Incorrect quantity. Quantity must be higher 0"));
        if (!orderService.isExists(offerOrderCardDTO.getOrderId())) reports.add(new BodyExceptionWrapper("e-003", "Order id="+offerOrderCardDTO.getOrderId()+" is not exists"));
        validateOffer(offerOrderCardDTO.getOfferId(), reports);
        return reports;
    }

    private void validateOffer(Long offerId, List<BodyExceptionWrapper> reports) {
        if (offerId==null) reports.add(new BodyExceptionWrapper("e-001", "Offer id must be not null"));
         else {
            ResponseEntity<Void> ent=client.get().uri("/offers/isExists/{id}", offerId).retrieve().toBodilessEntity().block();
            if (ent!=null && ent.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                reports.add(new BodyExceptionWrapper("e-003", "This offer is not exists"));
            }
        }
    }

    public List<BodyExceptionWrapper> validateExistsOfferOrderCard(OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        if (!orderService.isExists(offerOrderCardDTO.getOrderId())) reports.add(new BodyExceptionWrapper("e-003", "Order id="+offerOrderCardDTO.getOrderId()+" is not exists"));
        if (!offerOrderCardService.isExists(offerOrderCardDTO.getId())) reports.add(new BodyExceptionWrapper("e-003", "This card is not exists"));
        else reports.addAll(validateNewOfferOrderCard(offerOrderCardDTO));
        return reports;
    }

    public List<BodyExceptionWrapper> validateOfferOrderCardFromOrderValidator(OfferOrderCardDTO offerOrderCardDTO){
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        if (offerOrderCardDTO.getQuantity()<=0) reports.add(new BodyExceptionWrapper("e-003", "Incorrect quantity. Quantity must be higher 0"));
        validateOffer(offerOrderCardDTO.getOfferId(), reports);
        return reports;
    }
}
