package com.example.orders.serviceImpls;

import com.example.orders.DTOs.CatAndPriceDTO;
import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.entityes.OfferOrderCard;
import com.example.orders.entityes.Order;
import com.example.orders.exceptions.OfferNotFound;
import com.example.orders.repositories.OrderRepo;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.services.OrderService;
import com.example.orders.services.StatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final StatusService statusService;
    private final OrderRepo orderRepo;
    private final OfferOrderCardService offerOrderCardService;
    private  WebClient client;

    @Autowired
    @Qualifier("8080")
    public void setClient(WebClient client) {
        this.client = client;
    }

    public Order get(Long id){
        return orderRepo.findById(id).orElse(null);
    }

    public OrderDTO getDTO(Long id){
        return getDTOByObj(get(id));
    }

    public OrderDTO getDTOByObj(Order order){
        if (order==null) return null;
        return OrderDTO.builder().id(order.getId())
                .status(order.getStatus().getValue())
                .dob(order.getDob())
                .offerOrderCards(order.getOfferOrderCards().stream().map(offerOrderCardService::getDTOByObj).collect(Collectors.toList()))
                .build();
    }

    public List<OrderDTO> getAll(){
        return orderRepo.findAll().stream().map(this::getDTOByObj).collect(Collectors.toList());
    }

    public void add(OrderDTO orderDTO){
        Order order=Order.builder()
                .status(statusService.get(orderDTO.getStatus()))
                .dob(orderDTO.getDob())
                .offerOrderCards(new HashSet<>())
                .build();
        order=orderRepo.save(order);
        for (OfferOrderCardDTO dto:orderDTO.getOfferOrderCards()){
            dto.setOrderId(order.getId());
            order.getOfferOrderCards().add(offerOrderCardService.getObjByDTO(dto));
        }
        log.info("add order {}", orderDTO);
    }

    public boolean delete(Long id){
        if (orderRepo.existsById(id)){
            orderRepo.deleteById(id);
            log.info("delete order id={}", id);
            return true;
        }
        return false;
    }

    public void update(OrderDTO orderDTO){
        Order order=orderRepo.getById(orderDTO.getId());
        order.setStatus(statusService.get(orderDTO.getStatus()));
        order.setDob(orderDTO.getDob());
        for(OfferOrderCardDTO dto:orderDTO.getOfferOrderCards()){
            dto.setOrderId(order.getId());
            if (dto.getId()!=null && offerOrderCardService.isExists(dto.getId())){
                offerOrderCardService.update(dto);
            }else {
                order.getOfferOrderCards().add(offerOrderCardService.getObjByDTO(dto));
            }
        }
        orderRepo.save(order);
        log.info("update order {}", orderDTO);
    }

    public void changeStatus(Long orderId, Long statusId){
        Order order=orderRepo.getById(orderId);
        order.setStatus(statusService.getStatus(statusId));
        orderRepo.save(order);
        log.info("change order {} status from {} to {}", orderId, order.getStatus().getId(), statusId);
    }

    @Override
    public List<CatAndPriceDTO> getInfo(Long id) {
        OrderDTO orderDTO=getDTO(id);
        List<CatAndPriceDTO> list=new ArrayList<>();
        for (OfferOrderCardDTO dto:orderDTO.getOfferOrderCards()){
            try {
                WebClient.ResponseSpec response = client.get().uri("/offers/{id}/categoryAndPrice", dto.getOfferId()).retrieve();
                if (response.toBodilessEntity().block().getStatusCode().equals(HttpStatus.OK)) {
                    CatAndPriceDTO catAndPriceDTO = response.toEntity(CatAndPriceDTO.class).block().getBody();
                    catAndPriceDTO.setPrice(catAndPriceDTO.getPrice() * dto.getQuantity());
                    list.add(catAndPriceDTO);
                }
            }catch (WebClientException e){
                throw new OfferNotFound(dto.getOfferId());
            }
        }
        log.info("get all info for order id={}", id);
        return list;
    }

    @Override
    public long countByStatus(Long id){
        return orderRepo.countByStatus(statusService.getStatus(id));
    }

    @Override
    public boolean isExists(Long id) {
        return id!=null && orderRepo.findById(id).isPresent();
    }
}
