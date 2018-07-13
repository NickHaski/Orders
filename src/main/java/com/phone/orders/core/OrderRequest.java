package com.phone.orders.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class OrderRequest {

    @NotEmpty(message = "Name can't be empty.")
    private final String name;

    @NotEmpty(message = "Surname can't be empty.")
    private final String surname;

    @EmailParam
    private final String email;

    @NotNull
    private final List<PhoneOrder> phoneOrderList;

}
