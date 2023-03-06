package br.com.controlefinanceiro.backend.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Andre Souza
 * Permission Context???
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PERMISSIONS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID permissionId;
	
	private String name;
		
	@Column(nullable = false, unique = true)
	private String code;
	
	private String description;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private ServiceModel service;
	
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<PermissionContextModel> permissionsContexts;
	
	@Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate @PrePersist
    public void prepared() {
    	this.code = code == null ? null : StringUtils.deleteWhitespace(code).toLowerCase();
    }
    
}
