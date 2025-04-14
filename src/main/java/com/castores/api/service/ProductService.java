package com.castores.api.service;

import com.castores.api.dto.Response;
import com.castores.api.dto.requests.RequestCreateProduct;
import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.dto.requests.RequestUpdateProductQuantity;
import com.castores.api.exception.RepositoryException;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Response getAllProducts() throws RepositoryException;
    Response getProductById(Long id) throws RepositoryException;
    Response createProduct(RequestCreateProduct request) throws RepositoryException;
    Response updateProduct(Long id, RequestUpdateProduct request) throws RepositoryException;
    Response deleteProduct(Long id) throws RepositoryException;
    Response updateProductQuantity(String action, RequestUpdateProductQuantity request) throws RepositoryException;
}
