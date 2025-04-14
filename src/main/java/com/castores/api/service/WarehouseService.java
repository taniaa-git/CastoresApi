package com.castores.api.service;

import com.castores.api.dto.Response;
import com.castores.api.exception.RepositoryException;
import org.springframework.stereotype.Service;

@Service
public interface WarehouseService {
    Response getAllRegisters() throws RepositoryException;
}
