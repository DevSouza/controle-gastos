package br.com.controlefinanceiro.backend.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.controlefinanceiro.backend.dtos.MovementGroupDTO;
import br.com.controlefinanceiro.backend.enuns.Frequency;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "TB_MOVEMENT_GROUP")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementGroupModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movementGroupId;

    @Column(nullable = false, length = 50)
    private String name;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CategoryModel category;
    
    @Fetch(FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<MovementModel> movements = new ArrayList<>();

    private LocalDate firstDue;
    
    @Column(nullable = false)
    private boolean fixed; // If fixed { installments  = null } 
    private Integer installments = 1;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Frequency frequency;
    
    @Column(nullable = false)
    private UUID createdBy;
    
    @Column(nullable = false)
    private UUID updatedBy;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
   
    public MovementGroupDTO toMovementGroupDTO() {
    	return MovementGroupModel.toMovementGroupDTO(this);
    }
    
    public static MovementGroupDTO toMovementGroupDTO(MovementGroupModel movementGroup) {
    	var movementGroupDTO = new MovementGroupDTO();
    	BeanUtils.copyProperties(movementGroup, movementGroupDTO);
    	
    	return movementGroupDTO;
    }
    
}
