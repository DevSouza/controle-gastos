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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Table(name = "TB_SERVICES")
public class ServiceModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID serviceId;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<PermissionModel> permissions;
	
	@Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
}
