package com.example.orders.validators;

import com.example.orders.DTOs.AddressDTO;
import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.serviceImpls.OrderServiceImpl;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.services.OrderService;
import com.example.orders.services.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
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
        if(orderDTO.getDob().isAfter(LocalDate.now())) reports.add(new BodyExceptionWrapper("t-003", "Incorrect date"));
        if (!statusService.isExists(orderDTO.getStatus())) reports.add(new BodyExceptionWrapper("e-003", "This status is not exists"));
        validateCustomerAndAddress(orderDTO.getCustomerId(), orderDTO.getAddressId(), reports);
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

    private void validateCustomerAndAddress(Long customerId, Long addressId, List<BodyExceptionWrapper> reports) {
        if (customerId==null){
            reports.add(new BodyExceptionWrapper("e-001", "Customer id must be not null"));
            return;
        }
        if (addressId==null){
            reports.add(new BodyExceptionWrapper("e-001", "Address id must be not null"));
            return;
        }
        ResponseEntity<List<AddressDTO>> addresses=client8081.get().uri("/customers/{id}/addresses", customerId).retrieve().toEntityList(AddressDTO.class).block();
        if (addresses!=null && addresses.getStatusCode().equals(HttpStatus.NOT_FOUND)) reports.add(new BodyExceptionWrapper("e-003", "This customer is not exists"));
        else if(addresses!=null && addresses.getBody().stream().noneMatch(x->x.getId().equals(addressId))) reports.add(new BodyExceptionWrapper("e-003", "Wrong address to this customer"));
    }
}
