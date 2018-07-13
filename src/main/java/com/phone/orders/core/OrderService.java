package com.phone.orders.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderEntity createOrder(final String userName,
                                   final String userSurname,
                                   final String email,
                                   final Map<Long, Integer> phoneCount,
                                   final List<PhonePrice> priceList) {

        Assert.notEmpty(phoneCount, "phoneCount must not be null");
        Assert.notEmpty(priceList, "priceList must not be null");
        Assert.notNull(userName, "userName must not be null");
        Assert.notNull(userSurname, "userSurname must not be null");
        Assert.notNull(email, "email must not be null");
        throwIfDifferentCurrencies(priceList);


        final Map<Long, Integer> prices = getPriceToId(priceList);
        final Currency currency = priceList.get(0).getCurrency();

        final Set<PhoneOrderEntity> phoneOrders = getPhoneOrderEntities(phoneCount, prices, currency);
        final int totalPriceCents = getTotalPriceCents(phoneCount, prices);

        final OrderEntity order = new OrderEntity(userName, userSurname, email, phoneOrders, totalPriceCents, currency);
        return orderRepository.save(order);
    }

    public OrderEntity getOrderById(final Long id, final String key) {
        return orderRepository.findByUniqueKey(key);
    }

    private Map<Long, Integer> getPriceToId(final List<PhonePrice> priceList) {
        return priceList.stream().collect(Collectors.toMap(PhonePrice::getId, PhonePrice::getPriceCents));
    }

    private Set<PhoneOrderEntity> getPhoneOrderEntities(final Map<Long, Integer> phoneCount, final Map<Long, Integer> prices, final Currency currency) {
        return phoneCount.entrySet().stream()
                .map(o -> new PhoneOrderEntity(o.getKey(), o.getValue(), prices.get(o.getKey()), currency))
                .collect(Collectors.toSet());
    }

    private int getTotalPriceCents(final Map<Long, Integer> phoneCount, final Map<Long, Integer> prices) {
        return phoneCount.entrySet()
                .stream()
                .mapToInt(p -> prices.get(p.getKey()) * p.getValue())
                .sum();
    }


    private void throwIfDifferentCurrencies(final List<PhonePrice> priceList) {
        final Set<Currency> collect = priceList.stream().map(PhonePrice::getCurrency).collect(Collectors.toSet());
        if (collect.size() > 1) {
            throw new MultipleCurrenciesException(String.format("Multiple currencies in one order is unsupported. Currencies: %s", collect.toString()));
        }
    }
}
