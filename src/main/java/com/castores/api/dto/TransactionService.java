package com.castores.api.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import com.castores.api.utils.Constants;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionService {
    @Setter(AccessLevel.NONE)
    private LocalTime startTime;

    @Setter(AccessLevel.NONE)
    private LocalTime endTime;

    @Setter(AccessLevel.NONE)
    private String transactionID = String.format("%s-%s", UUID.randomUUID().toString(), new Date().getTime());

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String originException;

    @Getter(AccessLevel.NONE)
    private String codeReference;

    @Getter(AccessLevel.NONE)
    private String messageReference;

    @Setter(AccessLevel.NONE)
    private HttpStatus statusHttp;

    @Setter(AccessLevel.NONE)
    private Meta meta;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String originInternal = "Internal";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String originExternal = "External";

    @Getter(AccessLevel.NONE)
    private String nameReference;

    public void startTimeRequest() {
        this.startTime = LocalTime.now();
    }

    public void setMeta(int codeService, String msjeService) {
        createMeta(codeService, msjeService, null);
    }

    public void setMetaException(int codeService, String msjeService, String msjeException) {
        createMeta(codeService, msjeService, msjeException);
    }

    private void createMeta(int codeService, String msjeApp, String msjeException) {
        this.statusHttp = getHttpStatus(codeService);

        this.meta = new Meta();
        this.meta.setCodeService(codeService);
        this.meta.setMessageService(msjeApp);
        this.meta.setTransactionID(this.transactionID);
        this.meta.setTimestamp(LocalDateTime.now().toString());
        this.meta.setTimeDuration(getDurationTimeRequest());
        this.meta.setMessageException(msjeException);
        this.meta.setOriginException(this.originException);
        this.meta.setCodeReference(this.codeReference);
        this.meta.setMessageReference(this.messageReference);
        this.meta.setNameReference(this.nameReference);
        this.meta.setCodeHttp(this.statusHttp.value());
        this.meta.setMessageHttp(this.statusHttp.name());
    }

    private String getDurationTimeRequest() {
        this.endTime = LocalTime.now();
        return String.format("%s ms", Duration.between(this.startTime, this.endTime).toMillis());
    }

    private HttpStatus getHttpStatus(int codeService) {
        switch (codeService) {
            case Constants.CODE_SUCCESS -> {
                return HttpStatus.OK;
            }
            case Constants.CODE_ERROR_REQUEST_INVALID -> {
                this.originException = this.originInternal;
                return HttpStatus.BAD_REQUEST;
            }
            case Constants.CODE_ERROR_CLIENT_UNAUTHORIZED -> {
                this.originException = this.originInternal;
                return HttpStatus.UNAUTHORIZED;
            }
            case Constants.CODE_ERROR_CLIENT_FORBIDDEN -> {
                this.originException = this.originInternal;
                return HttpStatus.FORBIDDEN;
            }
            case Constants.CODE_ERROR_NOT_FOUND_PATH,
                 Constants.CODE_ERROR_NOT_FOUND_INFO -> {
                this.originException = codeService == Constants.CODE_ERROR_NOT_FOUND_INFO ? this.originExternal : this.originInternal;
                return HttpStatus.NOT_FOUND;
            }
            case Constants.CODE_ERROR_METHOD_NOT_ALLOWED -> {
                this.originException = this.originInternal;
                return HttpStatus.METHOD_NOT_ALLOWED;
            }
            case Constants.CODE_ERROR_REQUEST_TIMEOUT -> {
                this.originException = this.originExternal;
                return HttpStatus.REQUEST_TIMEOUT;
            }
            case Constants.CODE_ERROR_CONFLICT -> {
                this.originException = this.originExternal;
                return HttpStatus.CONFLICT;
            }
            case Constants.CODE_ERROR_UNPROCESSABLE_ENTITY -> {
                this.originException = this.originInternal;
                return HttpStatus.UNPROCESSABLE_ENTITY;
            }
            case Constants.CODE_ERROR_TOO_MANY_REQUESTS -> {
                this.originException = this.originInternal;
                return HttpStatus.TOO_MANY_REQUESTS;
            }
            case Constants.CODE_ERROR_SERVICE_INTERNAL -> {
                this.originException = this.originInternal;
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            case Constants.CODE_ERROR_SERVICE_UNAVAILABLE -> {
                this.originException = this.originExternal;
                return HttpStatus.SERVICE_UNAVAILABLE;
            }
            default -> {
                this.originException = this.originInternal;
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}