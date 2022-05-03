package com.example.orders.serviceImpls;

import com.example.orders.DTOs.CatAndPrice;
import com.example.orders.DTOs.CharacteristicDTO;
import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.entityes.OfferOrderCard;
import com.example.orders.exceptions.OfferNotFound;
import com.example.orders.repositories.OfferOrderCardRepo;
import com.example.orders.repositories.OrderRepo;
import com.example.orders.services.ChangedCharacteristicService;
import com.example.orders.services.OfferOrderCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OfferOrderCardServiceImpl implements OfferOrderCardService {
    private final OfferOrderCardRepo offerOrderCardRepo;
    private final OrderRepo orderRepo;
    private  WebClient client;

    private final ChangedCharacteristicService changedCharacteristicService;

    @Autowired
    @Qualifier("8080")
    public void setClient(WebClient client) {
        this.client = client;
    }

    public void add(OfferOrderCardDTO offerOrderCardDTO) {
        OfferOrderCard offerOrderCard = OfferOrderCard.builder()
                .offerId(offerOrderCardDTO.getOfferId())
                .quantity(offerOrderCardDTO.getQuantity())
                .thisOrder(orderRepo.getById(offerOrderCardDTO.getOrderId()))
                .build();
        offerOrderCardRepo.save(offerOrderCard);
        log.info("Add new card {}", offerOrderCardDTO);
    }

    public boolean delete(Long id) {
        if (offerOrderCardRepo.existsById(id)) {
            offerOrderCardRepo.deleteById(id);
            log.info("delete card id={}", id);
            return true;
        }
        return false;
    }

    public OfferOrderCard getObj(Long id) {
        return offerOrderCardRepo.findById(id).orElse(null);
    }

    public OfferOrderCardDTO getDTO(Long id) {
        OfferOrderCard offerOrderCard = offerOrderCardRepo.findById(id).orElse(null);
        return getDTOByObj(offerOrderCard);
    }

    public OfferOrderCardDTO getDTOByObj(OfferOrderCard offerOrderCard) {
        if (offerOrderCard == null) return null;
        return OfferOrderCardDTO.builder()
                .id(offerOrderCard.getId())
                .quantity(offerOrderCard.getQuantity())
                .offerId(offerOrderCard.getOfferId())
                .orderId(offerOrderCard.getThisOrder().getId())
                .build();
    }

    public OfferOrderCard getObjByDTO(OfferOrderCardDTO offerOrderCardDTO) {
        return OfferOrderCard.builder()
                .id(offerOrderCardDTO.getId())
                .quantity(offerOrderCardDTO.getQuantity())
                .offerId(offerOrderCardDTO.getOfferId())
                .thisOrder(orderRepo.getById(offerOrderCardDTO.getOrderId()))
                .build();
    }

    public List<OfferOrderCardDTO> getAll() {
        return offerOrderCardRepo.findAll().stream().map(this::getDTOByObj).collect(Collectors.toList());
    }

    public void update(OfferOrderCardDTO offerOrderCardDTO) {
        OfferOrderCard offerOrderCard = offerOrderCardRepo.getById(offerOrderCardDTO.getId());
        offerOrderCard.setQuantity(offerOrderCardDTO.getQuantity());
        offerOrderCard.setOfferId(offerOrderCardDTO.getOfferId());
        offerOrderCard.setThisOrder(orderRepo.getById(offerOrderCardDTO.getOrderId()));
        log.info("update card {}", getDTOByObj(offerOrderCard));
    }

    public long countWithOfferId(Long id) {
        return offerOrderCardRepo.countByOfferId(id);
    }

    @Override
    public boolean isExists(Long id) {
        return id != null && offerOrderCardRepo.existsById(id);
    }

    @Override
    public List<CharacteristicDTO> getInfo(Long id) {
        OfferOrderCardDTO cardDTO = getDTO(id);
        if (cardDTO==null) return null;
        List<CharacteristicDTO> list=null;
        try {
            WebClient.ResponseSpec response = client.get().uri("/offers/{id}/characteristics", cardDTO.getOfferId()).retrieve();
            if (response.toBodilessEntity().block().getStatusCode().equals(HttpStatus.OK)) {
                list=response.toEntityList(CharacteristicDTO.class).block().getBody();
                for (CharacteristicDTO dto:list){
                    dto.setCardId(id);
                    changedCharacteristicService.computeIfChanged(dto);
                }
            }
        } catch (WebClientException e) {
            throw new OfferNotFound(cardDTO.getOfferId());
        }
        log.info("get all info for card id={}", id);
        return list;
    }

    @Override
    public Double getPrice(OfferOrderCardDTO cardDTO) {
        List<CharacteristicDTO> list;
        double res=0;
        try {
            WebClient.ResponseSpec response = client.get().uri("/offers/{id}/characteristics", cardDTO.getOfferId()).retrieve();
            WebClient.ResponseSpec response1 = client.get().uri("/offers/{id}/categoryAndPrice", cardDTO.getOfferId()).retrieve();
            if (response.toBodilessEntity().block().getStatusCode().equals(HttpStatus.OK)) {
                list=response.toEntityList(CharacteristicDTO.class).block().getBody();
                res=response1.toEntity(CatAndPrice.class).block().getBody().getPrice();
                for (CharacteristicDTO dto:list){
                    dto.setCardId(cardDTO.getId());
                    res+=changedCharacteristicService.computeIfChanged(dto);
                }
            }
        } catch (WebClientException e) {
            throw new OfferNotFound(cardDTO.getOfferId());
        }
        return res*cardDTO.getQuantity();
    }
}
