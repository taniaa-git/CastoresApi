package com.castores.api.controller;

import com.castores.api.dto.Meta;
import com.castores.api.dto.Response;
import com.castores.api.dto.requests.RequestCreateProduct;
import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.exception.RepositoryException;
import com.castores.api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Response mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new Response();
        Meta meta = new Meta();
        meta.setCodeHttp(200);
        mockResponse.setMeta(meta);
        mockResponse.setData(null);
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(mockResponse);
        Response response = productController.getAllProducts();
        assertEquals(HttpStatus.OK.value(), response.getMeta().getCodeHttp());
    }

    @Test
    void testGetProductById() throws RepositoryException {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(mockResponse);
        Response response = productController.getProductById(productId);
        assertEquals(HttpStatus.OK.value(), response.getMeta().getCodeHttp());
    }

    @Test
    void testCreateProduct() throws RepositoryException {
        RequestCreateProduct request = new RequestCreateProduct();
        when(productService.createProduct(request)).thenReturn(mockResponse);
        Response response = productController.createProduct(request);
        assertEquals(HttpStatus.OK.value(), response.getMeta().getCodeHttp());
    }

    @Test
    void testUpdateProduct() throws RepositoryException {
        Long productId = 1L;
        RequestUpdateProduct request = new RequestUpdateProduct();
        when(productService.updateProduct(productId, request)).thenReturn(mockResponse);
        Response response = productController.updateProduct(productId, request);
        assertEquals(HttpStatus.OK.value(), response.getMeta().getCodeHttp());
    }

    @Test
    void testDeleteProduct() throws RepositoryException {
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(mockResponse);
        Response response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.OK.value(), response.getMeta().getCodeHttp());
    }
}