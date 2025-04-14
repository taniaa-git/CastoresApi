package com.castores.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {
    private int codeService;
    private String messageService;
    private String transactionID;
    private int codeHttp;
    private String messageHttp;
    private String timestamp;
    private String timeDuration;
    private String messageException;
    private String originException;
    private String nameReference;
    private String codeReference;
    private String messageReference;
}