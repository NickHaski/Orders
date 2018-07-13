package com.phone.orders.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private String name;
    private String surname;
    private String email;

    private List<PhoneOrder> phoneOrders;
    private OrderStatus orderStatus;
    private String totalPrice;


}
