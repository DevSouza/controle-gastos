package br.com.controlefinanceiro.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controlefinanceiro.backend.enuns.PermissionContext;
import br.com.controlefinanceiro.backend.models.PermissionContextModel;
import br.com.controlefinanceiro.backend.models.PermissionModel;

public interface PermissionContextRepository extends JpaRepository<PermissionContextModel, UUID> {
	
	Optional<PermissionContextModel> findByContextAndPermission(PermissionContext context, PermissionModel permission);
	
}
