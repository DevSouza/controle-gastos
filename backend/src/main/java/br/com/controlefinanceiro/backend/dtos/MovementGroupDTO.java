package br.com.controlefinanceiro.backend.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.enuns.Frequency;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementGroupDTO {
	
    private UUID movementGroupId;
    private String name;
    private CategoryDTO category;
    private LocalDate firstDue;
    private boolean fixed; // If fixed { installments  = null } 
    private Integer installments = 1;
    private Frequency frequency;
    private UUID createdBy;
    private UUID updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
