package com.example.orders.exceptions;

public class OfferNotFound extends RuntimeException{
    private Long id;

    public OfferNotFound(Long id) {
        this.id = id;
    }

    public OfferNotFound(String message, Long id) {
        super(message);
        this.id = id;
    }

    public OfferNotFound(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public OfferNotFound(Throwable cause, Long id) {
        super(cause);
        this.id = id;
    }
}
