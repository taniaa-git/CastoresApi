package com.castores.api.controller;

import com.castores.api.dto.Response;
import com.castores.api.dto.requests.RequestUpdateProductQuantity;
import com.castores.api.exception.RepositoryException;
import com.castores.api.service.ProductService;
import com.castores.api.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final ProductService productService;
    private final WarehouseService warehouseService;

    public WarehouseController(ProductService productService, WarehouseService warehouseService) {
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    @Operation(
            summary = "Only admin role allowed",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping
    public Response getAllRegisters() throws RepositoryException {
        return warehouseService.getAllRegisters();
    }

    @Operation(
            summary = "Only admin role allowed",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PutMapping("/addQuantity")
    @PreAuthorize("hasRole('ADMIN')")
    public Response addQuantity(@Valid @RequestBody RequestUpdateProductQuantity request) throws RepositoryException {
        return productService.updateProductQuantity("ADD", request);
    }

    @Operation(
            summary = "Only warehouse role allowed ",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PutMapping("/subtractQuantity")
    @PreAuthorize("hasRole('WAREHOUSE')")
    public Response subtractQuantity(@Valid @RequestBody RequestUpdateProductQuantity request) throws RepositoryException {
        return productService.updateProductQuantity("SUBTRACT", request);
    }

}
