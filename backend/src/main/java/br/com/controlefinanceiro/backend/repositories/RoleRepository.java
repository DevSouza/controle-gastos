package br.com.controlefinanceiro.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controlefinanceiro.backend.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
	
	Optional<RoleModel> findByName(String name);
	
}
