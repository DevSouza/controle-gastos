package br.com.controlefinanceiro.backend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controlefinanceiro.backend.models.MovementModel;

public interface MovementRepository extends JpaRepository<MovementModel, UUID> {
	
	@Query(value = "from MovementModel where createdBy = :userId and dueAt between :start and :end order by dueAt, createdAt")
	List<MovementModel> findByDueAtBetweenAndUserId(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("userId") UUID userId);

}
