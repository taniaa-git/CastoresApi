package com.castores.api.exception;

import com.castores.api.utils.Constants;
import com.castores.api.utils.Utilities;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.castores.api.configs.DistributedTransactions;
import com.castores.api.dto.Response;
import com.castores.api.dto.TransactionService;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
    @Resource(name = "getTransactionService")
    private TransactionService transactionservice;

    @Resource(name = "getUtilities")
    private Utilities utilities;

    @Resource(name = "getDistributedTransactions")
    private DistributedTransactions distributedtransactions;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ValidationErrorResponse response = new ValidationErrorResponse("Validation failed", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ServiceException.class, RepositoryException.class, HttpClientException.class})
    public ResponseEntity<Object> handleApplicationAppException(Exception exception) {
        return triggerResponseException(exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(Constants.MSJE_ERROR_CLIENT_UNAUTHORIZED, HttpStatus.UNAUTHORIZED); // 403
    }

    @ExceptionHandler(value = {Exception.class, IllegalArgumentException.class, ClassNotFoundException.class, IllegalStateException.class, RuntimeException.class})
    public ResponseEntity<Object> globalExceptionHandler(Exception exception, WebRequest webRequest) {
        return triggerResponseException(exception);
    }

    private ResponseEntity<Object> triggerResponseException(Exception exception) {
        try {
            this.distributedtransactions.rollbackTransactions();

            if (exception instanceof RepositoryException exRepository) {
                this.transactionservice.setNameReference("DATABASE");
                this.transactionservice.setCodeReference("NA");
                this.transactionservice.setMessageReference("NA");
                this.transactionservice.setMetaException(exRepository.getCode(), exRepository.getMsje(), exRepository.getMsjeException());
            } else if (exception instanceof HttpClientException exHttpClient) {
                this.transactionservice.setNameReference(exHttpClient.getNameDependency());
                this.transactionservice.setCodeReference(exHttpClient.getCodeDependency());
                this.transactionservice.setMessageReference(exHttpClient.getMsjeDependency());
                this.transactionservice.setMetaException(exHttpClient.getCode(), exHttpClient.getMsje(), exHttpClient.getMsjeException());
            } else if (exception instanceof ServiceException exService) {
                this.transactionservice.setNameReference("NA");
                this.transactionservice.setCodeReference("NA");
                this.transactionservice.setMessageReference("NA");
                this.transactionservice.setMetaException(exService.getCode(), exService.getMsje(), exService.getMsjeException());
            } else {
                this.transactionservice.setNameReference("NA");
                this.transactionservice.setCodeReference("NA");
                this.transactionservice.setMessageReference("NA");
                this.transactionservice.setMetaException(Constants.CODE_ERROR_SERVICE_INTERNAL, Constants.MSJE_ERROR_SERVICE_INTERNAL, exception.getMessage());
            }

            Response response = new Response();
            response.setMeta(this.transactionservice.getMeta());

            log.debug("Response{}", this.utilities.objectToJson(response));

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return new ResponseEntity<>(response, httpHeaders, this.transactionservice.getStatusHttp());
        } finally {
            log.debug("triggerResponseException Finally");
        }
    }
}