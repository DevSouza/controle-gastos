package br.com.controlefinanceiro.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controlefinanceiro.backend.models.PermissionModel;

public interface PermissionRepository extends JpaRepository<PermissionModel, UUID> {

	Optional<PermissionModel> findByName(String name);
	
}
