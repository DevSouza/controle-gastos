package br.com.controlefinanceiro.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controlefinanceiro.backend.models.ServiceModel;

public interface ServiceRepository extends JpaRepository<ServiceModel, UUID> {

	Optional<ServiceModel> findByName(String name);
	
}
