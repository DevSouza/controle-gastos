package br.com.controlefinanceiro.backend.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class ExceptionDetails {
    protected String title;
    protected String details;
    protected String developerMessage;
    protected String code;
    protected LocalDateTime timestamp;
}
