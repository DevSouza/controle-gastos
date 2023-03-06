package br.com.controlefinanceiro.backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtDto {

	@NonNull
	private String accessToken;
	
	private String refreshToken;
	@NonNull
	private String tokenType;
	
}
