package com.castores.api.exception;

import org.springframework.core.NestedRuntimeException;

import lombok.Getter;

@Getter
public class RepositoryException extends NestedRuntimeException {
    private final int code;
    private final String msje;
    private final String msjeException;

    public RepositoryException(int code, String msje, Throwable cause) {
        super(msje, cause);
        this.code = code;
        this.msje = msje;

        if (cause.getMessage() == null) {
            this.msjeException = cause.getCause().getMessage().replace("\"", "");
        } else {
            this.msjeException = cause.getMessage().replace("\"", "");
        }
    }

    public RepositoryException(int code, String msje, String msjeException) {
        super(msje);
        this.code = code;
        this.msje = msje;
        this.msjeException = msjeException;
    }

}
