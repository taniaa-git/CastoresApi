package com.castores.api.service.impl;

import com.castores.api.dto.Data;
import com.castores.api.dto.Response;
import com.castores.api.dto.TransactionService;
import com.castores.api.dto.requests.RequestCreateProduct;
import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.dto.requests.RequestUpdateProductQuantity;
import com.castores.api.entities.Product;
import com.castores.api.exception.RepositoryException;
import com.castores.api.model.ProductModel;
import com.castores.api.service.ProductService;
import com.castores.api.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource(name = "getTransactionService")
    private TransactionService transactionService;

    private final ProductModel productModel;

    public ProductServiceImpl(ProductModel productModel, TransactionService transactionService) {
        this.productModel = productModel;
        this.transactionService = transactionService;
    }

    @Override
    public Response getAllProducts() throws RepositoryException{
        Response response = new Response();
        try {
            List<Product> list = getProducts();
            if (list == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
            Data data = new Data();
            data.setProductsList(list);
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, e);
        }
        return response;
    }

    @Override
    public Response getProductById(Long id) throws RepositoryException{
        Response response = new Response();
        try {
            Product product = getById(id);
            if (product == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
            Data data = new Data();
            data.setProduct(product);
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, e);
        }
        return response;
    }

    @Override
    public Response createProduct(RequestCreateProduct request) throws RepositoryException{
        Response response = new Response();
        try {
            Product product = createEntity(request);
            Product insertedProduct = insert(product);
            if (insertedProduct == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_SAVE_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_SAVE_SUCCESS);
            Data data = new Data();
            data.setProduct(insertedProduct);
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_SAVE_DATABASE, e);
        }
        return response;
    }

    @Override
    public Response updateProduct(Long id, RequestUpdateProduct request) throws RepositoryException{
        Response response = new Response();
        try {
            Product updatedProduct = update(id, request);
            if (updatedProduct == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_UPDATE_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_UPDATE_SUCCESS);
            Data data = new Data();
            data.setProduct(updatedProduct);
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_UPDATE_DATABASE, e);
        }
        return response;
    }

    @Override
    public Response deleteProduct(Long id) throws RepositoryException{
        Response response = new Response();
        try {
            Product deletedProduct = delete(id);
            if (deletedProduct == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_DELETE_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_DELETE_SUCCESS);
            Data data = new Data();
            data.setMessage("Product deleted successfully");
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_DELETE_DATABASE, e);
        }
        return response;
    }

    @Override
    public Response updateProductQuantity(String action, RequestUpdateProductQuantity request) throws RepositoryException {
        Response response = new Response();
        try {
            Product updatedProduct = updateQuantity(action, request);
            if (updatedProduct == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_UPDATE_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_UPDATE_SUCCESS);
            Data data = new Data();
            data.setProduct(updatedProduct);
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_UPDATE_DATABASE, e);
        }
        return response;
    }

    private List<Product> getProducts() throws RepositoryException {
        List<Product> list;
        try {
            list = this.productModel.getAllProducts();
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, e);
        }
        return list;
    }

    private Product getById(Long id) throws RepositoryException {
        Product element;
        try {
            element = this.productModel.getProductById(id);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, e);
        }
        return element;
    }


    private Product insert(Product product) throws RepositoryException {
        Product element;
        try {
            element = productModel.createProduct(product);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_SAVE_DATABASE, e);
        }
        return element;
    }

    private Product updateQuantity(String action, RequestUpdateProductQuantity request) throws RepositoryException {
        Product element;
        try {
            element = productModel.updateProductQuantity(action,request);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_UPDATE_DATABASE, e);
        }
        return element;
    }

    private Product update(Long id, RequestUpdateProduct request) throws RepositoryException {
        Product element;
        try {
            element = productModel.updateProduct(id, request);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_UPDATE_DATABASE, e);
        }
        return element;
    }

    private Product delete(Long id) throws RepositoryException {
        Product element;
        try {
            element = productModel.deleteProduct(id);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_DELETE_DATABASE, e);
        }
        return element;
    }

    private Product createEntity(RequestCreateProduct request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(0);
        return product;
    }
}
