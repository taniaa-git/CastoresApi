package com.castores.api.model;

import com.castores.api.dto.requests.RequestUpdateProduct;
import com.castores.api.dto.requests.RequestUpdateProductQuantity;
import com.castores.api.entities.Product;
import com.castores.api.entities.User;
import com.castores.api.entities.WarehouseLog;
import com.castores.api.exception.RepositoryException;
import com.castores.api.repository.ProductRepository;
import com.castores.api.repository.UserRepository;
import com.castores.api.repository.WarehouseLogRepository;
import com.castores.api.utils.Constants;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class ProductModel {

    private final ProductRepository productRepository;
    private final WarehouseLogRepository warehouseLogRepository;
    private final UserRepository userRepository;

    public ProductModel(ProductRepository productRepository,WarehouseLogRepository warehouseLogRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.warehouseLogRepository = warehouseLogRepository;
        this.userRepository = userRepository;
    }

    public List<Product> getAllProducts() throws DataAccessException {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws DataAccessException {
        return productRepository.findFirstById(id);
    }

    public Product createProduct(Product product) throws DataAccessException {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, RequestUpdateProduct request) throws DataAccessException {
        Product existingProduct = productRepository.findFirstByIdAndDeletedAtIsNull(id);
        if (existingProduct == null) {
            throw new RepositoryException(Constants.CODE_ERROR_NOT_FOUND_INFO, Constants.MSJE_ERROR_UPDATE_DATABASE, "");
        }
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        return productRepository.save(existingProduct);
    }

    public Product deleteProduct(Long id) throws DataAccessException {
        Product existingProduct = productRepository.findFirstById(id);
        if (existingProduct == null) {
            throw new RepositoryException(Constants.CODE_ERROR_NOT_FOUND_INFO, Constants.MSJE_ERROR_DELETE_DATABASE, "");
        }
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        if(existingProduct.getDeletedAt() != null){
            currentTimestamp = null;
        }
        existingProduct.setDeletedAt(currentTimestamp);
        return productRepository.save(existingProduct);
    }

    public Product updateProductQuantity(String action, RequestUpdateProductQuantity request) throws DataAccessException{
        Product existingProduct = productRepository.findFirstByIdAndDeletedAtIsNull(request.getId());
        if (existingProduct == null) {
            throw new RepositoryException(Constants.CODE_ERROR_NOT_FOUND_INFO, Constants.MSJE_ERROR_UPDATE_DATABASE, "");
        }

        int actualQuantity = existingProduct.getQuantity();
        if((actualQuantity <  request.getQuantity()) && action.equals("SUBTRACT")){
            throw new RepositoryException(Constants.CODE_ERROR_REQUEST_INVALID, Constants.MSJE_ERROR_CLIENT_UNAUTHORIZED, "Quantity to subtract greater than existing");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findFirstByUsername(username);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        WarehouseLog log = new WarehouseLog();
        log.setQuantity(request.getQuantity());
        log.setProduct(existingProduct);
        log.setUser(currentUser);
        log.setTransactionDate(currentTimestamp);
        log.setAction(action);

        if (action.equals("ADD")){
            existingProduct.setQuantity(actualQuantity + request.getQuantity());
        }else{
            existingProduct.setQuantity(actualQuantity - request.getQuantity());
        }

        Product product = productRepository.save(existingProduct);
        warehouseLogRepository.save(log);
        return product;
    }
}
