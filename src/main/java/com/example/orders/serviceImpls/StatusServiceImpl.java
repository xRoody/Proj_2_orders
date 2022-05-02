package com.example.orders.serviceImpls;

import com.example.orders.DTOs.StatusDTO;
import com.example.orders.entityes.Status;
import com.example.orders.repositories.StatusRepo;
import com.example.orders.services.StatusService;
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
public class StatusServiceImpl implements StatusService {
    private final StatusRepo statusRepo;

    public void addNewStatus(String value){
        if (!statusRepo.existsByValue(value)){
            statusRepo.save(Status.builder().value(value).build());
            log.info("add new status value={}", value);
        }
    }

    public boolean delete(String value){
        if (statusRepo.existsByValue(value)){
            statusRepo.deleteByValue(value);
            log.info("delete status value={}", value);
            return true;
        }
        return false;
    }

    public void update(StatusDTO statusDTO){
        Status status=statusRepo.getById(statusDTO.getId());
        status.setValue(statusDTO.getValue());
        statusRepo.save(status);
        log.info("update status {}", statusDTO);
    }

    public Status getStatus(Long id){
        return statusRepo.findById(id).orElse(null);
    }

    public StatusDTO getStatusValue(Long id){
        Status status=statusRepo.findById(id).orElse(null);
        return status==null?null:new StatusDTO(status.getId(),status.getValue());
    }

    public List<StatusDTO> getAllStatusesValues(){
        return statusRepo.findAll().stream().map(x->new StatusDTO(x.getId(), x.getValue())).collect(Collectors.toList());
    }

    public boolean isExists(String value){
        return statusRepo.existsByValue(value);
    }

    public boolean delete(Long id){
        if (statusRepo.existsById(id)){
            statusRepo.deleteById(id);
            log.info("delete status id={}", id);
            return true;
        }
        return false;
    }

    public Status get(String value){
        return statusRepo.getStatusByValue(value);
    }
}
