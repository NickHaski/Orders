package com.phone.orders.core;

public class UnsupportedCurrencyException extends RuntimeException {

    UnsupportedCurrencyException(final String message) {
        super(message);
    }

}
