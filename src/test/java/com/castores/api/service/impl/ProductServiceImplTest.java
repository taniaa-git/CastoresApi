package com.castores.api.service.impl;

import com.castores.api.dto.Meta;
import com.castores.api.dto.Response;
import com.castores.api.dto.TransactionService;
import com.castores.api.dto.requests.RequestCreateProduct;
import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.entities.Product;
import com.castores.api.exception.RepositoryException;
import com.castores.api.model.ProductModel;
import com.castores.api.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductModel productModel;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private RequestCreateProduct requestCreateProduct;
    private RequestUpdateProduct requestUpdateProduct;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.getId();

        requestCreateProduct = new RequestCreateProduct();
        requestCreateProduct.setName("New Product");
        requestCreateProduct.setDescription("New Description");
        requestCreateProduct.setPrice(200.0);

        requestUpdateProduct = new RequestUpdateProduct();
        requestUpdateProduct.setName("Updated Product");
        requestUpdateProduct.setDescription("Updated Description");
        requestUpdateProduct.setPrice(300.0);
    }

    @Test
    void testGetAllProducts_Success() throws RepositoryException {
        List<Product> productList = Arrays.asList(product, product);
        when(productModel.getAllProducts()).thenReturn(productList);
        when(transactionService.getMeta()).thenReturn(new Meta());
        doNothing().when(transactionService).setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
        Response response = productService.getAllProducts();
        assertEquals(Constants.CODE_SUCCESS, response.getMeta().getCodeHttp());
        assertNotNull(response.getData());
        assertEquals(productList, response.getData().getProductsList());
        verify(productModel, times(1)).getAllProducts();
    }

    @Test
    void testGetAllProducts_EmptyList() throws RepositoryException {
        when(productModel.getAllProducts()).thenReturn(Collections.emptyList());
        when(transactionService.getMeta()).thenReturn(new Meta());
        doNothing().when(transactionService).setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
        Response response = productService.getAllProducts();
        assertEquals(Constants.CODE_SUCCESS, response.getMeta().getCodeHttp());
        assertNotNull(response.getData());
        assertTrue(response.getData().getProductsList().isEmpty());
        verify(productModel, times(1)).getAllProducts();
    }

    @Test
    void testGetAllProducts_RepositoryException() {
        when(productModel.getAllProducts()).thenThrow(new DataAccessException("Database error") {
        });
        RepositoryException exception = assertThrows(RepositoryException.class, productService::getAllProducts);
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_READ_DATABASE, exception.getMessage());
        verify(productModel, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById_Success() throws RepositoryException {
        when(productModel.getProductById(1L)).thenReturn(product);
        when(transactionService.getMeta()).thenReturn(new Meta());
        doNothing().when(transactionService).setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
        Response response = productService.getProductById(1L);
        assertEquals(Constants.CODE_SUCCESS, response.getMeta().getCodeHttp());
        assertNotNull(response.getData());
        assertEquals(product, response.getData().getProduct());
        verify(productModel, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productModel.getProductById(1L)).thenReturn(null);
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.getProductById(1L));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_READ_DATABASE, exception.getMessage());
        verify(productModel, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductById_RepositoryException() {
        when(productModel.getProductById(1L)).thenThrow(new DataAccessException("Database error") {
        });
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.getProductById(1L));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_READ_DATABASE, exception.getMessage());
        verify(productModel, times(1)).getProductById(1L);
    }

    @Test
    void testCreateProduct_Success() throws RepositoryException {
        when(productModel.createProduct(any(Product.class))).thenReturn(product);
        when(transactionService.getMeta()).thenReturn(new Meta());
        doNothing().when(transactionService).setMeta(Constants.CODE_SUCCESS, Constants.MSJE_SAVE_SUCCESS);
        Response response = productService.createProduct(requestCreateProduct);
        assertEquals(Constants.CODE_SUCCESS, response.getMeta().getCodeHttp());
        assertNotNull(response.getData());
        assertEquals(product, response.getData().getProduct());
        verify(productModel, times(1)).createProduct(any(Product.class));
    }

    @Test
    void testCreateProduct_RepositoryException() {
        when(productModel.createProduct(any(Product.class))).thenThrow(new DataAccessException("Database error") {
        });
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.createProduct(requestCreateProduct));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_SAVE_DATABASE, exception.getMessage());
        verify(productModel, times(1)).createProduct(any(Product.class));
    }

    @Test
    void testUpdateProduct_Success() throws RepositoryException {
        when(productModel.updateProduct(1L, requestUpdateProduct)).thenReturn(product);
        when(transactionService.getMeta()).thenReturn(new Meta());
        doNothing().when(transactionService).setMeta(Constants.CODE_SUCCESS, Constants.MSJE_UPDATE_SUCCESS);
        Response response = productService.updateProduct(1L, requestUpdateProduct);
        assertEquals(Constants.CODE_SUCCESS, response.getMeta().getCodeHttp());
        assertNotNull(response.getData());
        assertEquals(product, response.getData().getProduct());
        verify(productModel, times(1)).updateProduct(1L, requestUpdateProduct);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productModel.updateProduct(1L, requestUpdateProduct)).thenReturn(null);
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.updateProduct(1L, requestUpdateProduct));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_UPDATE_DATABASE, exception.getMessage());
        verify(productModel, times(1)).updateProduct(1L, requestUpdateProduct);
    }

    @Test
    void testUpdateProduct_RepositoryException() {
        when(productModel.updateProduct(1L, requestUpdateProduct)).thenThrow(new DataAccessException("Database error") {
        });
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.updateProduct(1L, requestUpdateProduct));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_UPDATE_DATABASE, exception.getMessage());
        verify(productModel, times(1)).updateProduct(1L, requestUpdateProduct);
    }

    @Test
    void testDeleteProduct_Success() throws RepositoryException {
        when(productModel.deleteProduct(1L)).thenReturn(product);
        when(transactionService.getMeta()).thenReturn(new Meta());
        doNothing().when(transactionService).setMeta(Constants.CODE_SUCCESS, Constants.MSJE_DELETE_SUCCESS);
        Response response = productService.deleteProduct(1L);
        assertEquals(Constants.CODE_SUCCESS, response.getMeta().getCodeHttp());
        assertNotNull(response.getData());
        assertEquals("Product deleted successfully", response.getData().getMessage());
        verify(productModel, times(1)).deleteProduct(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productModel.deleteProduct(1L)).thenReturn(null);
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.deleteProduct(1L));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_DELETE_DATABASE, exception.getMessage());
        verify(productModel, times(1)).deleteProduct(1L);
    }

    @Test
    void testDeleteProduct_RepositoryException() {
        when(productModel.deleteProduct(1L)).thenThrow(new DataAccessException("Database error") {
        });
        RepositoryException exception = assertThrows(RepositoryException.class, () -> productService.deleteProduct(1L));
        assertEquals(Constants.CODE_ERROR_SERVICE_INTERNAL, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_DELETE_DATABASE, exception.getMessage());
        verify(productModel, times(1)).deleteProduct(1L);
    }
}