package com.castores.api.exception;

import org.springframework.core.NestedRuntimeException;

import lombok.Getter;

@Getter
public class ServiceException extends NestedRuntimeException {
    private final int code;
    private final String msje;
    private final String msjeException;

    public ServiceException(int code, String msje, Throwable cause) {
        super(msje, cause);
        this.code = code;
        this.msje = msje;

        if (cause.getMessage() == null) {
            this.msjeException = cause.getCause().getMessage().replace("\"", "");
        } else {
            this.msjeException = cause.getMessage().replace("\"", "");
        }
    }

    public ServiceException(int code, String msje) {
        super(msje);
        this.code = code;
        this.msje = msje;
        this.msjeException = "";
    }

}
