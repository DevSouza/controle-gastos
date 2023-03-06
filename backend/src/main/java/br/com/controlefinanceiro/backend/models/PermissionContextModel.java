package br.com.controlefinanceiro.backend.models;

import java.time.LocalDateTime;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.controlefinanceiro.backend.enuns.PermissionContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Andre Souza
 * Permission Context???
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PERMISSION_CONTEXT", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"permission_id", "context"})
})
public class PermissionContextModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID permissionContextId;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PermissionContext context;
	
	/* ADD FIELD TO CONFIGURATION PER CONTEXT */
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private PermissionModel permission;
	
	@Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
}
