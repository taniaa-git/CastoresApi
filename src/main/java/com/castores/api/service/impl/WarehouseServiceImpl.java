package com.castores.api.service.impl;

import com.castores.api.dto.Data;
import com.castores.api.dto.Response;
import com.castores.api.dto.TransactionService;
import com.castores.api.entities.WarehouseLog;
import com.castores.api.exception.RepositoryException;
import com.castores.api.repository.WarehouseLogRepository;
import com.castores.api.service.WarehouseService;
import com.castores.api.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    @Resource(name = "getTransactionService")
    private TransactionService transactionService;

    private final WarehouseLogRepository warehouseLogRepository;

    public WarehouseServiceImpl(TransactionService transactionService, WarehouseLogRepository warehouseLogRepository) {
        this.transactionService = transactionService;
        this.warehouseLogRepository = warehouseLogRepository;
    }

    @Override
    public Response getAllRegisters() throws RepositoryException {
        Response response = new Response();
        try {
            List<WarehouseLog> list = getProducts();
            if (list == null) {
                throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, "");
            }
            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
            Data data = new Data();
            data.setWarehouseLogs(list);
            response.setMeta(this.transactionService.getMeta());
            response.setData(data);
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, e);
        }
        return response;
    }

    private List<WarehouseLog> getProducts() throws RepositoryException {
        List<WarehouseLog> list;
        try {
            list = this.warehouseLogRepository.findAll();
        } catch (DataAccessException e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_READ_DATABASE, e);
        }
        return list;
    }
}
