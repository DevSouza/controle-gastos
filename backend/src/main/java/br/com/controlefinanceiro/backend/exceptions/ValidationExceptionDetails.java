package br.com.controlefinanceiro.backend.exceptions;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
    private final String fields;
    private final String fieldsMessage;
    
    private final List<ValidationError> inner;
    
}
