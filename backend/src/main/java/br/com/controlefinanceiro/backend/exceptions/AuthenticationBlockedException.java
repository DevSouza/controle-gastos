package br.com.controlefinanceiro.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class AuthenticationBlockedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AuthenticationBlockedException(String message) {
        super(message);
    }
}
