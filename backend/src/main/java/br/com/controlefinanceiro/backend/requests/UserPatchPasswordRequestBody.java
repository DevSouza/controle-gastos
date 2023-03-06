package br.com.controlefinanceiro.backend.requests;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPatchPasswordRequestBody {

	@NotBlank
	private String oldPassword;
	
	@NotBlank
	private String password;
	
}
