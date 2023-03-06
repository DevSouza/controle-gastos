package br.com.controlefinanceiro.backend.exceptions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class BadRequestExceptionDetails extends ExceptionDetails {
	private final List<ValidationError> inner;
}