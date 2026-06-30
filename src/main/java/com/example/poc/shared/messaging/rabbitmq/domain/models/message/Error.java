package com.example.poc.shared.messaging.rabbitmq.domain.models.message;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Error {
  @JsonInclude(NON_NULL)
  private String field;

  private String message;

  public Error(String message) {
    this.field = null;
    this.message = message;
  }

  public Error(ConstraintViolation<?> violation) {
    this.field = violation.getPropertyPath().toString();
    this.message = violation.getMessage();
  }
}