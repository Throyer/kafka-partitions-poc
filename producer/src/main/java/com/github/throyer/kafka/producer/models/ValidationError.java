package com.github.throyer.kafka.producer.models;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Objects.nonNull;

import java.util.List;

import org.springframework.validation.BindException;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ValidationError {
  @JsonInclude(NON_NULL)
  private final String field;

  private final String message;
  
  public String simplify() {
    var result = new StringBuilder();
    
    if (nonNull(this.field)) {
      result.append(String.format("field: '%s', ", field));
    }
    
    result.append(String.format("message: '%s'", message));
      
    return result.toString();
  }

  public ValidationError(String message) {
    this.field = null;
    this.message = message;
  }

  public ValidationError(org.springframework.validation.FieldError error) {
    this.field = error.getField();
    this.message = error.getDefaultMessage();
  }

  public ValidationError(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public static List<ValidationError> of(BindException exception) {
    return exception.getBindingResult()
      .getAllErrors()
      .stream()
      .map((error) -> (new ValidationError((org.springframework.validation.FieldError) error)))
      .toList();
  }
}