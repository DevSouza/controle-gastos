package br.com.controlefinanceiro.backend.models;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import lombok.Data;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DREModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID categoryId;
	
	@Enumerated(EnumType.STRING)
	private TypeCategory type;
	
	private String name;
	
	private BigDecimal jan = BigDecimal.ZERO;
	private BigDecimal feb = BigDecimal.ZERO;
	private BigDecimal mar = BigDecimal.ZERO;
	private BigDecimal apr = BigDecimal.ZERO;
	private BigDecimal may = BigDecimal.ZERO;
	private BigDecimal june = BigDecimal.ZERO;
	private BigDecimal july = BigDecimal.ZERO;
	private BigDecimal aug = BigDecimal.ZERO;
	private BigDecimal sept = BigDecimal.ZERO;
	private BigDecimal oct = BigDecimal.ZERO;
	private BigDecimal nov = BigDecimal.ZERO;
	private BigDecimal dec = BigDecimal.ZERO;

    
}
