package com.phone.orders.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotEmpty(message = "Name can't be empty.")
    private String name;

    @NotEmpty(message = "Surname can't be empty.")
    private String surname;

    @EmailParam
    private String email;

    @NotNull
    private List<PhoneOrder> phoneOrderList;

}
