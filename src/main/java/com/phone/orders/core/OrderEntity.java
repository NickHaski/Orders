package com.phone.orders.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_INFO")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "UNIQUE_KEY")
    private String uniqueKey;

    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    @Column(name = "CURRENCY")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_entity_id")
    private Set<PhoneOrderEntity> orderedPhones;

    OrderEntity(final String userName,
                final String userSurname,
                final String email,
                final Set<PhoneOrderEntity> orderedPhones,
                final int totalPriceCents,
                final Currency currency) {
        this.name = userName;
        this.surname = userSurname;
        this.email = email;
        this.orderedPhones = orderedPhones;
        this.uniqueKey = UUID.randomUUID().toString();
        this.orderStatus = OrderStatus.CREATED;
        this.totalPrice = totalPriceCents;
        this.currency = currency;
    }


    public void setOrderedPhones(final Set<PhoneOrderEntity> orderedPhones) {
        this.orderedPhones.addAll(orderedPhones);
    }

    public Set<PhoneOrderEntity> getOrderedPhones() {
        return orderedPhones;
    }

}
