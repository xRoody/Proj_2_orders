package com.example.orders.serviceImpls;

import com.example.orders.DTOs.CharacteristicDTO;
import com.example.orders.DTOs.OfferOrderCardDTO;
import com.example.orders.DTOs.OrderDTO;
import com.example.orders.entityes.Order;
import com.example.orders.repositories.OrderRepo;
import com.example.orders.services.OfferOrderCardService;
import com.example.orders.services.OrderService;
import com.example.orders.services.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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
    public long countByStatus(Long id){
        return orderRepo.countByStatus(statusService.getStatus(id));
    }

    @Override
    public boolean isExists(Long id) {
        return id!=null && orderRepo.findById(id).isPresent();
    }

    public Double getPrice(Long id){
        OrderDTO orderDTO=getDTO(id);
        if (orderDTO==null) return null;
        Double res=0.0;
        for(OfferOrderCardDTO dto:orderDTO.getOfferOrderCards()) {
            res += offerOrderCardService.getPrice(dto);
        }
        return res;
    }
}
