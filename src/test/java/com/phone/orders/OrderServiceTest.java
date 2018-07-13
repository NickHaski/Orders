package com.phone.orders;

import com.phone.orders.core.OrderEntity;
import com.phone.orders.core.OrderRepository;
import com.phone.orders.core.OrderService;
import com.phone.orders.core.OrderStatus;
import com.phone.orders.core.PhoneOrderEntity;
import com.phone.orders.core.PhonePrice;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.phone.orders.core.Currency.EUR;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderService(orderRepository);
    }

    @Test
    public void shouldReturnOrderById() {

        final OrderEntity orderEntity = getOrderEntity();
        when(orderRepository.findByUniqueKey(eq("123"))).thenReturn(orderEntity);
        final OrderEntity orderById = orderService.getOrderById(1L, "123");

        Assert.assertNotNull(orderById);
        Assert.assertEquals(EUR, orderById.getCurrency());
        Assert.assertEquals("name", orderById.getName());
        Assert.assertEquals("surname", orderById.getSurname());
        Assert.assertEquals("email@email.com", orderById.getEmail());
        Assert.assertEquals(1000, orderById.getTotalPrice());
        Assert.assertEquals(OrderStatus.CREATED, orderById.getOrderStatus());

    }

    @Test
    public void shouldCreateOrder() {

        final ArgumentCaptor<OrderEntity> argument = ArgumentCaptor.forClass(OrderEntity.class);

        final Map<Long, Integer> idToCount = new HashMap<>();
        idToCount.put(1L, 1);
        idToCount.put(2L, 1);

        final List<PhonePrice> phonePrices = new ArrayList<>();
        phonePrices.add(new PhonePrice(1L, 1000, EUR));
        phonePrices.add(new PhonePrice(2L, 100, EUR));

        orderService.createOrder("name", "surname", "email@gmail.com", idToCount, phonePrices);

        verify(orderRepository).save(argument.capture());
        Assert.assertEquals(1100, argument.getValue().getTotalPrice());
    }

    private OrderEntity getOrderEntity() {
        final PhoneOrderEntity phoneOrderEntity = new PhoneOrderEntity();
        final Set<PhoneOrderEntity> phoneOrderEntities = new HashSet<>();
        phoneOrderEntities.add(phoneOrderEntity);
        return new OrderEntity(1L,
                "name",
                "surname",
                "email@email.com",
                "123",
                1000,
                EUR,
                OrderStatus.CREATED
                ,
                phoneOrderEntities
        );
    }

}
