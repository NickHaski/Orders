package com.phone.orders.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity findByUniqueKey( final String key);
}
