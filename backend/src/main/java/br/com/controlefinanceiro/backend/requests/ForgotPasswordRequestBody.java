package br.com.controlefinanceiro.backend.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgotPasswordRequestBody {

	@Email
	@NotBlank
	private String email;
	
}
