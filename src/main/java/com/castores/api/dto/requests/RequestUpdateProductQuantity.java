package com.castores.api.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUpdateProductQuantity {
    @NotNull(message = "price required")
    private Long id;
    @NotNull(message = "quantity required")
    @Positive(message = "quantity must be a positive number")
    @DecimalMin(value = "1", message = "price must be at least 1")
    private int quantity;
}
