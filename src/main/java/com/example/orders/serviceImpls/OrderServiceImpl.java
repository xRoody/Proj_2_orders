package com.example.orders.serviceImpls;

import com.example.orders.repositories.OrderRepo;
import com.example.orders.services.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl {
    private final StatusService statusService;
    public final OrderRepo orderRepo;

    public long countByStatus(Long id){
        return orderRepo.countByStatus(statusService.getStatus(id));
    }
}
