package com.example.orders.serviceImpls;

import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.entityes.OfferOrderCard;
import com.example.orders.repositories.OfferOrderCardRepo;
import com.example.orders.repositories.OrderRepo;
import com.example.orders.services.OfferOrderCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OfferOrderCardServiceImpl implements OfferOrderCardService {
    private final OfferOrderCardRepo offerOrderCardRepo;
    private final OrderRepo orderRepo;


    public void add(OfferOrderCardDTO offerOrderCardDTO){
        OfferOrderCard offerOrderCard=OfferOrderCard.builder()
                .offerId(offerOrderCardDTO.getOfferId())
                .quantity(offerOrderCardDTO.getQuantity())
                .thisOrder(orderRepo.getById(offerOrderCardDTO.getOrderId()))
                .build();
        offerOrderCardRepo.save(offerOrderCard);
        log.info("Add new card {}", offerOrderCardDTO);
    }

    public boolean delete(Long id){
        if (offerOrderCardRepo.existsById(id)){
            offerOrderCardRepo.deleteById(id);
            log.info("delete card id={}", id);
            return true;
        }
        return false;
    }

    public OfferOrderCard getObj(Long id){
        return offerOrderCardRepo.findById(id).orElse(null);
    }

    public OfferOrderCardDTO getDTO(Long id){
        OfferOrderCard offerOrderCard=offerOrderCardRepo.findById(id).orElse(null);
        return getDTOByObj(offerOrderCard);
    }

    public OfferOrderCardDTO getDTOByObj(OfferOrderCard offerOrderCard){
        if (offerOrderCard==null) return null;
        return OfferOrderCardDTO.builder()
                .id(offerOrderCard.getId())
                .quantity(offerOrderCard.getQuantity())
                .offerId(offerOrderCard.getOfferId())
                .orderId(offerOrderCard.getThisOrder().getId())
                .build();
    }

    public OfferOrderCard getObjByDTO(OfferOrderCardDTO offerOrderCardDTO){
        return OfferOrderCard.builder()
                .id(offerOrderCardDTO.getId())
                .quantity(offerOrderCardDTO.getQuantity())
                .offerId(offerOrderCardDTO.getOfferId())
                .thisOrder(orderRepo.getById(offerOrderCardDTO.getOrderId()))
                .build();
    }

    public List<OfferOrderCardDTO> getAll(){
        return offerOrderCardRepo.findAll().stream().map(this::getDTOByObj).collect(Collectors.toList());
    }

    public void update(OfferOrderCardDTO offerOrderCardDTO){
        OfferOrderCard offerOrderCard=offerOrderCardRepo.getById(offerOrderCardDTO.getId());
        offerOrderCard.setQuantity(offerOrderCardDTO.getQuantity());
        offerOrderCard.setOfferId(offerOrderCardDTO.getOfferId());
        offerOrderCard.setThisOrder(orderRepo.getById(offerOrderCardDTO.getOrderId()));
        log.info("update card {}", getDTOByObj(offerOrderCard));
    }

    public long countWithOfferId(Long id){
        return offerOrderCardRepo.countByOfferId(id);
    }

    @Override
    public boolean isExists(Long id) {
        return id != null && offerOrderCardRepo.existsById(id);
    }
}
