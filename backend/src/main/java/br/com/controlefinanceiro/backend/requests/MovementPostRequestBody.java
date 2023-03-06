package br.com.controlefinanceiro.backend.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import br.com.controlefinanceiro.backend.enuns.Frequency;
import lombok.Data;

@Data
public class MovementPostRequestBody {
    
	private UUID userId;
	private String name;
	private LocalDate dueAt;
	@NotNull
	private UUID categoryId;
	private boolean fixed;
	private Frequency frequency;
	private Integer installments;

	private BigDecimal amount;
	
}
