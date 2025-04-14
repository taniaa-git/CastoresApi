package com.castores.api.model;

import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.entities.Product;
import com.castores.api.exception.RepositoryException;
import com.castores.api.repository.ProductRepository;
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
public class ProductModelTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductModel productModel;

    private Product product;
    private RequestUpdateProduct requestUpdateProduct;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);

        requestUpdateProduct = new RequestUpdateProduct();
        requestUpdateProduct.setName("Updated Product");
        requestUpdateProduct.setDescription("Updated Description");
        requestUpdateProduct.setPrice(200.0);
    }

    @Test
    void testGetAllProducts_Success() {
        List<Product> productList = Arrays.asList(product, product);
        when(productRepository.findByDeletedAtIsNull()).thenReturn(productList);

        List<Product> result = productModel.getAllProducts();

        assertNotNull(result);
        assertEquals(productList, result);
        verify(productRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    void testGetAllProducts_EmptyList() {
        when(productRepository.findByDeletedAtIsNull()).thenReturn(Collections.emptyList());

        List<Product> result = productModel.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    void testGetAllProducts_DataAccessException() {
        when(productRepository.findByDeletedAtIsNull()).thenThrow(new DataAccessException("Database error") {});

        DataAccessException exception = assertThrows(DataAccessException.class, productModel::getAllProducts);
        assertEquals("Database error", exception.getMessage());
        verify(productRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findFirstById(1L)).thenReturn(product);

        Product result = productModel.getProductById(1L);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository, times(1)).findFirstById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findFirstById(1L)).thenReturn(null);

        Product result = productModel.getProductById(1L);

        assertNull(result);
        verify(productRepository, times(1)).findFirstById(1L);
    }

    @Test
    void testGetProductById_DataAccessException() {
        when(productRepository.findFirstById(1L)).thenThrow(new DataAccessException("Database error") {});

        DataAccessException exception = assertThrows(DataAccessException.class, () -> productModel.getProductById(1L));
        assertEquals("Database error", exception.getMessage());
        verify(productRepository, times(1)).findFirstById(1L);
    }

    @Test
    void testCreateProduct_Success() {
        when(productRepository.save(product)).thenReturn(product);

        Product result = productModel.createProduct(product);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testCreateProduct_DataAccessException() {
        when(productRepository.save(product)).thenThrow(new DataAccessException("Database error") {});

        DataAccessException exception = assertThrows(DataAccessException.class, () -> productModel.createProduct(product));
        assertEquals("Database error", exception.getMessage());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findFirstByIdAndDeletedAtIsNull(1L)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productModel.updateProduct(1L, requestUpdateProduct);

        assertNotNull(result);
        assertEquals(product, result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(200.0, result.getPrice());
        verify(productRepository, times(1)).findFirstByIdAndDeletedAtIsNull(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findFirstByIdAndDeletedAtIsNull(1L)).thenReturn(null);

        RepositoryException exception = assertThrows(RepositoryException.class, () -> productModel.updateProduct(1L, requestUpdateProduct));
        assertEquals(Constants.CODE_ERROR_NOT_FOUND_INFO, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_UPDATE_DATABASE, exception.getMessage());
        verify(productRepository, times(1)).findFirstByIdAndDeletedAtIsNull(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_DataAccessException() {
        when(productRepository.findFirstByIdAndDeletedAtIsNull(1L)).thenThrow(new DataAccessException("Database error") {});

        DataAccessException exception = assertThrows(DataAccessException.class, () -> productModel.updateProduct(1L, requestUpdateProduct));
        assertEquals("Database error", exception.getMessage());
        verify(productRepository, times(1)).findFirstByIdAndDeletedAtIsNull(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findFirstByIdAndDeletedAtIsNull(1L)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productModel.deleteProduct(1L);

        assertNotNull(result);
        assertNotNull(result.getDeletedAt());
        verify(productRepository, times(1)).findFirstByIdAndDeletedAtIsNull(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findFirstByIdAndDeletedAtIsNull(1L)).thenReturn(null);

        RepositoryException exception = assertThrows(RepositoryException.class, () -> productModel.deleteProduct(1L));
        assertEquals(Constants.CODE_ERROR_NOT_FOUND_INFO, exception.getCode());
        assertEquals(Constants.MSJE_ERROR_DELETE_DATABASE, exception.getMessage());
        verify(productRepository, times(1)).findFirstByIdAndDeletedAtIsNull(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_DataAccessException() {
        when(productRepository.findFirstByIdAndDeletedAtIsNull(1L)).thenThrow(new DataAccessException("Database error") {});

        DataAccessException exception = assertThrows(DataAccessException.class, () -> productModel.deleteProduct(1L));
        assertEquals("Database error", exception.getMessage());
        verify(productRepository, times(1)).findFirstByIdAndDeletedAtIsNull(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
}