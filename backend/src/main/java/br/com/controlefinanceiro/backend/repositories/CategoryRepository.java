package br.com.controlefinanceiro.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controlefinanceiro.backend.models.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {
	
	List<CategoryModel> findByCreatedBy(UUID createdBy);
}
