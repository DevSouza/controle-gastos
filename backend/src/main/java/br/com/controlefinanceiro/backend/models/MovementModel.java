package br.com.controlefinanceiro.backend.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.controlefinanceiro.backend.dtos.MovementDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "TB_MOVEMENT")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movementId;

    @Column(nullable = false, length = 50)
    private String name; 
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "movement_group_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MovementGroupModel group;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = true)
    private Integer installment;
    
    @Column(nullable = false)
    private LocalDate dueAt;
    
    @Column(nullable = true)
    private LocalDateTime paidAt;
    
    @Column(nullable = false)
    private UUID createdBy;
    
    @Column(nullable = false)
    private UUID updatedBy;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public MovementDTO toMovementDTO() {
    	return MovementModel.toMovementDTO(this);
    }
    
    public static MovementDTO toMovementDTO(MovementModel movement) {
    	var movementDTO = new MovementDTO();
    	BeanUtils.copyProperties(movement, movementDTO);
    	
    	movementDTO.setCategory(movement.getGroup().getCategory().toCategoryDTO());
    	movementDTO.setGroup(movement.getGroup().toMovementGroupDTO());
    	
    	return movementDTO;
    }
    
}
