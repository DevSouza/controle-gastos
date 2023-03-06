package br.com.controlefinanceiro.backend.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private List<ValidationError> inner;

	public BadRequestException(String message) {
        super(message);
    }
	
	public BadRequestException(String message, List<ValidationError> inner) {
        super(message);
        this.inner = inner;
    }

	public List<ValidationError> getInner() {
		return inner;
	}

}
