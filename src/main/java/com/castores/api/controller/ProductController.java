package com.castores.api.controller;

import com.castores.api.dto.Response;
import com.castores.api.dto.requests.RequestCreateProduct;
import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.exception.RepositoryException;
import com.castores.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productServices) {
        this.productService = productServices;
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping
    public Response getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping("/{id}")
    public Response getProductById(@PathVariable Long id) throws RepositoryException {
        return productService.getProductById(id);
    }

    @Operation(
            summary = "Only admin role allowed",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response createProduct(@Valid @RequestBody RequestCreateProduct request) throws RepositoryException {
        return productService.createProduct(request);
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @PutMapping("/{id}")
    public Response updateProduct(@PathVariable Long id, @Valid @RequestBody RequestUpdateProduct request) throws RepositoryException {
        return productService.updateProduct(id, request);
    }

    @Operation(
            summary = "Only admin role allowed",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @DeleteMapping("/{id}")
    public Response deleteProduct(@PathVariable Long id) throws RepositoryException {
        return productService.deleteProduct(id);
    }
}