package com.phone.orders.core;

public class MultipleCurrenciesException extends RuntimeException {

    MultipleCurrenciesException(final String message) {
        super(message);
    }
}
