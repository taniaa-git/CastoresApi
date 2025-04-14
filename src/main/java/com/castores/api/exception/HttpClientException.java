package com.castores.api.exception;

import org.springframework.core.NestedRuntimeException;

import lombok.Getter;

@Getter
public class HttpClientException extends NestedRuntimeException {
    private final int code;
    private final String msje;
    private final String nameDependency;
    private final String codeDependency;
    private final String msjeDependency;
    private final String msjeException;

    public HttpClientException(int code, String msje, String nameDependency, String codeDependency, String msjeDependency, Throwable cause) {
        super(msje, cause);

        this.code = code;
        this.msje = msje;

        if (cause.getMessage() == null) {
            this.msjeException = cause.getCause().getMessage().replace("\"", "");
        } else {
            this.msjeException = cause.getMessage().replace("\"", "");
        }

        this.nameDependency = nameDependency;
        this.codeDependency = codeDependency;
        this.msjeDependency = msjeDependency;
    }

    public HttpClientException(int code, String msje, String nameDependency, String codeDependency, String msjeDependency, String msjeException) {
        super(msje);

        this.code = code;
        this.msje = msje;
        this.msjeException = msjeException.replace("\"", "");
        this.nameDependency = nameDependency;
        this.codeDependency = codeDependency;
        this.msjeDependency = msjeDependency;
    }

}

