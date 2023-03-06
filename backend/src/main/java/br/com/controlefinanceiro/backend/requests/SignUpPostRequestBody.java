package br.com.controlefinanceiro.backend.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.validation.EmailValidation;
import br.com.controlefinanceiro.backend.validation.UsernameValidation;
import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpPostRequestBody {

    @NotBlank(message = "Username é obrigatório")
    @UsernameValidation
    @Size(min = 4, max = 60, message = "Tamanho deve estar entre {min} e {max}")
    private String username;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    @EmailValidation
    @Size(min = 4, max = 60, message = "Tamanho deve estar entre {min} e {max}")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @ToString.Exclude
    @Size(min = 6, max = 20, message = "Tamanho deve estar entre {min} e {max}")
    private String password;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 60, message = "Tamanho deve estar entre {min} e {max}")
    private String fullName;

}
