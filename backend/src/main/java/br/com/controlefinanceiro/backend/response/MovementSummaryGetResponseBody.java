package br.com.controlefinanceiro.backend.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementSummaryGetResponseBody {
    
	private LocalDate start;
	private LocalDate end;
	private BigDecimal expense;
	private BigDecimal revenue;
	private BigDecimal result;
	private LocalDate date;
}
