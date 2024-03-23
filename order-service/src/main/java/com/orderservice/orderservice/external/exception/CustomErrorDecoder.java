package com.orderservice.orderservice.external.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.orderservice.exception.CustomException;
import com.orderservice.orderservice.external.response.ErrorResponseMessage;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        try {
            ErrorResponseMessage errorResponseMessage = objectMapper.readValue(response.body().
                    asInputStream(), ErrorResponseMessage.class);

            return new CustomException(errorResponseMessage.getErrorMessage(),
                    errorResponseMessage.getErrorCode(),
                    response.status());
        } catch (IOException e) {
            throw new CustomException("Internal Server Error", "INTERNAL_SERVER_ERROR", 500);
        }
    }
}
