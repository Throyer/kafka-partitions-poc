package com.github.throyer.rabbitmq.producer.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Setter
@Getter
@NoArgsConstructor
public class ApiError {
  private String message;

  private Integer status;

  @JsonInclude(NON_NULL)
  private Collection<ValidationError> errors;

  public ApiError(String message, Integer status) {
    this.message = message;
    this.status = status;
    this.errors = null;
  }

  public ApiError(String message, HttpStatusCode status) {
    this.message = message;
    this.status = status.value();
    this.errors = null;
  }

  public ApiError(HttpStatusCode status, Collection<ValidationError> errors) {
    this.message = "Confira o campo 'errors' para mais detalhes.";
    this.status = status.value();
    this.errors = errors;
  }

  public ApiError(HttpStatusCode status, String error) {
    this.message = "Confira o campo 'errors' para mais detalhes.";
    this.status = status.value();
    this.errors = List.of(new ValidationError(error));
  }

  public ApiError(HttpStatusCode status, ValidationError error) {
    this.message = "Confira o campo 'errors' para mais detalhes.";
    this.status = status.value();
    this.errors = List.of(error);
  }
}

