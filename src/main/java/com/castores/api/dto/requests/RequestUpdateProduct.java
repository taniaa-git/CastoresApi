package com.castores.api.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUpdateProduct {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "description required")
    private String description;
    @NotNull(message = "price required")
    @Positive(message = "price must be a positive number")
    @DecimalMin(value = "0.01", message = "price must be at least 0.01")
    private Double price;
}
