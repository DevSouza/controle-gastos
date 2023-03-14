package br.com.controlefinanceiro.backend.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.dtos.CategoryDTO;
import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CATEGORY")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryModel implements Serializable {
    private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID categoryId;

    @Column(nullable = false, length = 50)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCategory type;
    
    @Column(nullable = true)
    private BigDecimal limitMaxPercentage;
    @Column(nullable = true)
    private BigDecimal limitMaxValue;

    @Column(nullable = false)
    private UUID createdBy;
    
    @Column(nullable = false)
    private UUID updatedBy;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public CategoryDTO toCategoryDTO() {
    	return CategoryModel.toCategoryDTO(this);
    }
    
    public static CategoryDTO toCategoryDTO(CategoryModel category) {
    	var categoryDTO = new CategoryDTO();
    	BeanUtils.copyProperties(category, categoryDTO);
    	
    	return categoryDTO;
    }
    
}
