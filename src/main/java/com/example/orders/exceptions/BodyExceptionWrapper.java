package com.example.orders.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BodyExceptionWrapper {
    private String code;
    private String reason;
}
