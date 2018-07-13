package com.phone.orders.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final OrderConverter orderConverter;
    private final PhoneService phoneService;

    @Value("${own.host}")
    private String ownHost;

    @PostMapping(path = RestPath.Order.ROOT)
    private ResponseEntity<Void> createOrder(final @Valid @RequestBody OrderRequest order) {

        final Map<Long, Integer> phoneToCount = getOrderedPhones(order);
        final List<PhonePrice> phonePrices = phoneService.getPhonePrices(phoneToCount.keySet());

        final OrderEntity createdOrder = orderService.createOrder(order.getName(), order.getSurname(), order.getEmail(), phoneToCount, phonePrices);
        return ResponseEntity.created(getOrderUri(createdOrder)).build();
    }

    @GetMapping(path = RestPath.Order.ROOT + "/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable(name = "id") final  long id,
            @RequestParam final  String key) {

        final OrderEntity order = orderService.getOrderById(id, key);
        return ResponseEntity.ok(orderConverter.convert(order));
    }

    private Map<Long, Integer> getOrderedPhones(final OrderRequest order) {
        return order.getPhoneOrderList().stream().collect(Collectors.toMap(PhoneOrder::getId, PhoneOrder::getCount));
    }

    private URI getOrderUri(final OrderEntity createdOrder) {
        return UriComponentsBuilder.fromHttpUrl(ownHost)
                .pathSegment(RestPath.Order.ROOT)
                .pathSegment(createdOrder.getId().toString())
                .queryParam("key", createdOrder.getUniqueKey())
                .build()
                .toUri();
    }
}
