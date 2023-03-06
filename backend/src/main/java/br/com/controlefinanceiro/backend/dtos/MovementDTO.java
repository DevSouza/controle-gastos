package br.com.controlefinanceiro.backend.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementDTO {
	
    private UUID movementId;
    private String name; 
    private MovementGroupDTO group;
    private CategoryDTO category;
    private BigDecimal amount;
    private LocalDate dueAt;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
}
