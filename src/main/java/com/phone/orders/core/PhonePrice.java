package com.phone.orders.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhonePrice {

    private final Long id;
    private final int priceCents;
    private final Currency currency;

}
