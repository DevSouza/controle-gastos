package br.com.controlefinanceiro.backend.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserPutRequestBody {
    
    @Size(min = 4, max = 60)
    private String username;

    @Email
    @Size(min = 4, max = 60)
    private String email;
    
    @Size(min = 3, max = 60)
    private String fullName;
}
