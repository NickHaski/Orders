package com.phone.orders.core;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestPath {

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Order {
        public static final String ROOT = "/orders";
    }

}
