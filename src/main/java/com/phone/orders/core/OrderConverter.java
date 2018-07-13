package com.phone.orders.core;

import com.phone.orders.util.MoneyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderConverter implements Converter<OrderEntity, OrderResponse> {

    @Override
    public OrderResponse convert(final OrderEntity orderEntity) {
        final int totalPrice = orderEntity.getTotalPrice();
        final Set<PhoneOrderEntity> orderedPhones = orderEntity.getOrderedPhones();
        final List<PhoneOrder> phones = getPhoneOrders(orderedPhones);

        return OrderResponse.builder()
                .name(orderEntity.getName())
                .surname(orderEntity.getSurname())
                .email(orderEntity.getEmail())
                .totalPrice(MoneyConverter.toEuro(totalPrice))
                .phoneOrders(phones)
                .orderStatus(orderEntity.getOrderStatus())
                .build();
    }

    private List<PhoneOrder> getPhoneOrders(final Set<PhoneOrderEntity> orderedPhones) {
        return orderedPhones.stream()
                .map(this::getPhoneOrder)
                .collect(Collectors.toList());
    }

    private PhoneOrder getPhoneOrder(final PhoneOrderEntity p) {
        return PhoneOrder.builder()
                .count(p.getCount())
                .id(p.getId())
                .build();
    }
}
