package br.com.controlefinanceiro.backend.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryPutRequestBody {
    
	@NotBlank
    @Size(min = 4, max = 60)
    private String name;

    private BigDecimal limitMaxPercentage;
    private BigDecimal limitMaxValue;
}
