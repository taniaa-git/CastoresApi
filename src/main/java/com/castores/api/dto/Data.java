package com.castores.api.dto;

import com.castores.api.entities.Product;
import com.castores.api.entities.WarehouseLog;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {
    private Product product;
    private List<Product> productsList;
    private String message;
    private String token;
    private List<WarehouseLog> warehouseLogs;
}

