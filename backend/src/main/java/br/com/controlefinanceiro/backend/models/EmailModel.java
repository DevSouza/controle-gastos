package br.com.controlefinanceiro.backend.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.enuns.StatusEmail;
import br.com.controlefinanceiro.backend.enuns.TypeEmail;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_EMAILS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailModel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID emailId;
	
	@Column(nullable = false)
	private UUID integrationId;
	
	@Column(nullable = false)
	private String emailFrom;
	
	@Column(nullable = false)
	private String emailTo;
	
	@Column(nullable = false)
	private String subject;
	
	@Column(columnDefinition = "TEXT", nullable =  false)
	private String body;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeEmail type;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEmail status = StatusEmail.PROCESSING;
	
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("UTC"));
	
	@Column(nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now(ZoneId.of("UTC"));;
	private LocalDateTime sentAt;
	
	@Transient
	public boolean isHTML() {
		return type.equals(TypeEmail.HTML);
	}
}
