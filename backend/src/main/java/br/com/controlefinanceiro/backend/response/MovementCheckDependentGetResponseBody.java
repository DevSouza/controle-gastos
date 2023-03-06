package br.com.controlefinanceiro.backend.response;

import java.util.List;

import br.com.controlefinanceiro.backend.dtos.MovementDTO;
import lombok.Data;

@Data
public class MovementCheckDependentGetResponseBody {
	
	private boolean hasDependents = false;
	private List<MovementDTO> movements;
	
}
