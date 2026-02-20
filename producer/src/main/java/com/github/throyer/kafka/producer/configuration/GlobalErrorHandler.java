package com.github.throyer.kafka.producer.configuration;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.throyer.kafka.producer.models.ApiError;
import com.github.throyer.kafka.producer.models.ValidationError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {
  @ResponseStatus(code = BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ApiError badRequest(MissingServletRequestParameterException exception) {
    log.debug("global error handler. bad request. error: {}", exception.getMessage());
    return new ApiError(BAD_REQUEST, new ValidationError(exception.getParameterName(), exception.getMessage()));
  }

  @ResponseStatus(code = BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public ApiError badRequest(BindException exception) {
    log.debug("global error handler. bad request. error: {}", exception.getMessage());
    return new ApiError(BAD_REQUEST, ValidationError.of(exception));
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  @ResponseStatus(code = BAD_REQUEST)
  public ApiError badRequestFromRequestHeader(MissingRequestHeaderException exception) {
    log.debug("global error handler. bad request. error: {}", exception.getMessage());
    return new ApiError(BAD_REQUEST, exception.getMessage());
  }
}
