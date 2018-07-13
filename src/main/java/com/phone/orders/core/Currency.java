package com.phone.orders.core;

import java.util.Arrays;

public enum Currency {

    EUR;

    public static Currency getByName(final String value) {
        return Arrays.stream(values()).filter(v -> value.equalsIgnoreCase(v.name()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedCurrencyException("Unsupported currency."));
    }

}
