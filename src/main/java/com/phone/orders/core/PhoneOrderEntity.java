package com.phone.orders.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PHONE_ORDER")
public class PhoneOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PHONE_ID")
    private Long phoneId;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "PRICE_CENTS")
    private int priceCents;

    @Column(name = "CURRENCY")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "order_entity_id")
    private OrderEntity orderEntity;

    PhoneOrderEntity(final Long phoneId, final int count, final int priceCents, final Currency currency) {
        this.count = count;
        this.phoneId = phoneId;
        this.priceCents = priceCents;
        this.currency = currency;
    }

}
