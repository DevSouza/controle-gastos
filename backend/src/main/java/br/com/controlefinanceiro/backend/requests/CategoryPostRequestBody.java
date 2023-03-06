package br.com.controlefinanceiro.backend.requests;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import lombok.Data;

@Data
public class CategoryPostRequestBody {
    
	@NotBlank
    @Size(min = 4, max = 60)
    private String name;

	@NotNull
    private UUID userId;
	
	@NotNull
	private TypeCategory type;
    
    private BigDecimal limitMaxPercentage;
    private BigDecimal limitMaxValue;
}
