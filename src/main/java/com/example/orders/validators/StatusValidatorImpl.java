package com.example.orders.validators;

import com.example.orders.DTOs.StatusDTO;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.services.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class StatusValidatorImpl implements StatusValidator{
    private final StatusService statusService;
    private static final Pattern VALUE_PATTERN=Pattern.compile("[A-Z][a-z\\d\\s]*");

    @Override
    public List<BodyExceptionWrapper> validateNewStatus(StatusDTO statusDTO) {
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        validateValue(statusDTO.getValue(), reports);
        return reports;
    }

    public List<BodyExceptionWrapper> validateUpdatedStatus(StatusDTO statusDTO) {
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        if (!statusDTO.getValue().equals(statusService.getStatus(statusDTO.getId()).getValue()))validateValue(statusDTO.getValue(), reports);
        return reports;
    }

    private void validateValue(String s, List<BodyExceptionWrapper> reports){
        if (s==null || s.isBlank()) reports.add(new BodyExceptionWrapper("e-001", "Value must be not blank"));
        else if(statusService.isExists(s)) reports.add(new BodyExceptionWrapper("e-003", "This status already exists"));
        else if (!VALUE_PATTERN.matcher(s).matches()) reports.add(new BodyExceptionWrapper("e-002", "Incorrect value format"));
    }
}
