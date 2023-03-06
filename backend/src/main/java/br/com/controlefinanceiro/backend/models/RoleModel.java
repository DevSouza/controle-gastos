package br.com.controlefinanceiro.backend.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ROLES")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;
	
	@Column(nullable = false, unique = true)
	private String name;
	private String description;
	
	@Builder.Default
	@ManyToMany(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(
    	name = "TB_ROLE_PERMISSIONS_CONTEXT",
    	joinColumns = @JoinColumn(name = "role_id"),
    	inverseJoinColumns = @JoinColumn(name = "permission_context_id"))
    private Set<PermissionContextModel> permissions = new HashSet<>();
	
	@Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
