package br.com.controlefinanceiro.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controlefinanceiro.backend.models.DREModel;

public interface DRERepository extends JpaRepository<DREModel, UUID> {
	
	@Query(
			value =	" SELECT "+
					"	tc.category_id, " +
					"	tc.type, " +
					"	tc.name, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '01' THEN tm.amount ELSE 0 END) AS jan, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '02' THEN tm.amount ELSE 0 END) AS feb, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '03' THEN tm.amount ELSE 0 END) AS mar, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '04' THEN tm.amount ELSE 0 END) AS apr, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '05' THEN tm.amount ELSE 0 END) AS may, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '06' THEN tm.amount ELSE 0 END) AS june, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '07' THEN tm.amount ELSE 0 END) AS july, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '08' THEN tm.amount ELSE 0 END) AS aug, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '09' THEN tm.amount ELSE 0 END) AS sept, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '10' THEN tm.amount ELSE 0 END) AS oct, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '11' THEN tm.amount ELSE 0 END) AS nov, " +
					"	SUM(CASE WHEN to_char(date(tm.due_at),'MM') = '12' THEN tm.amount ELSE 0 END) AS \"dec\" " +
					" FROM tb_category tc " +
					"	LEFT JOIN tb_movement_group tmg ON tc.category_id = tmg.category_id " +
					"	LEFT JOIN tb_movement tm ON tmg.movement_group_id = tm.movement_group_id " +
					" WHERE " +
					"	tc.created_by = :userId AND " +
					"	tmg.created_by = :userId AND " +
					"	TO_CHAR(DATE(tm.due_at),'YYYY') = :year OR tm IS NULL " +
					" GROUP BY tc.category_id " +
					" ORDER BY tc.type desc, tc.name",
			nativeQuery = true
	)
	List<DREModel> findByYearAndUserId(@Param("year") String year, @Param("userId") UUID userId);

}
