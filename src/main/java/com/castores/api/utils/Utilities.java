package com.castores.api.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import com.google.gson.Gson;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Utilities {

    public <T> String objectToJson(T item) {
        return new Gson().toJson(item).replace('\t', '_').replace('\n', '_').replace('\r', '_');
    }

    @SuppressWarnings("unchecked")
    public <T> T jsonToObject(String json, Class<?> clazz) {
        return (T) new Gson().fromJson(json, clazz);
    }

    public <T> String marshall(T soapRequest) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(soapRequest.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.marshal(soapRequest, writer);
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshall(String xml, Class<?> clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StreamSource streamSource = new StreamSource(new StringReader(xml));
        return (T) jaxbUnmarshaller.unmarshal(streamSource);
    }

    public boolean retryOnException(Throwable ex, String name) {
        boolean result;

        if (ex instanceof HttpClientErrorException httpEx) {
            result = retryValidateException(httpEx.getStatusCode().value(), name);
        } else if (ex instanceof DataAccessException databaseEx) {
            if (databaseEx.getRootCause() instanceof SQLException sqlEx) {
                result = retryValidateException(sqlEx.getErrorCode(), name);
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    public boolean circuitBreakerOnException(Throwable ex, String name, boolean flagEnabled) {
        boolean result;

        if (flagEnabled) {
            if (ex instanceof HttpClientErrorException httpEx) {
                result = circuitbreakerHttpClientException(httpEx.getStatusCode().value(), name);
            } else if (ex instanceof ResourceAccessException) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    private boolean retryValidateException(int code, String name) {
        boolean result;

        if (name.equals(Constants.RESILIENCE4J_SSO)) {
            result = validateCodeExceptionResilence(code, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (name.equals(Constants.RESILIENCE4J_PGSQL)) {
            result = validateCodeExceptionResilence(code, 40000);
        } else {
            result = false;
        }

        return result;
    }

    private boolean circuitbreakerHttpClientException(int codeStatusHttp, String name) {
        boolean result;

        if (name.equals(Constants.RESILIENCE4J_SSO)) {
            result = validateCodeExceptionResilence(codeStatusHttp, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.SERVICE_UNAVAILABLE.value());
        } else {
            result = false;
        }

        return result;
    }

    private boolean validateCodeExceptionResilence(int code, Integer... codes) {
        boolean result = false;

        for (int idx = 0; idx < codes.length; idx++) {
            if (code == codes[idx]) {
                result = true;
                break;
            }
        }

        return result;
    }
}