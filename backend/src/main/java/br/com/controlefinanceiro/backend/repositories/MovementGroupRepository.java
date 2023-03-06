package br.com.controlefinanceiro.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controlefinanceiro.backend.models.MovementGroupModel;

public interface MovementGroupRepository extends JpaRepository<MovementGroupModel, UUID> {
	
	@Query(value = "from MovementGroupModel m where createdBy = :userId and m.category.categoryId = :categoryId")
	List<MovementGroupModel> findByCategoryIdAndUserId(@Param("categoryId") UUID categoryId, @Param("userId") UUID userId);
}
