package br.com.controlefinanceiro.backend.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controlefinanceiro.backend.exceptions.NotFoundException;
import br.com.controlefinanceiro.backend.models.MovementGroupModel;
import br.com.controlefinanceiro.backend.models.MovementModel;
import br.com.controlefinanceiro.backend.repositories.CategoryRepository;
import br.com.controlefinanceiro.backend.repositories.MovementGroupRepository;
import br.com.controlefinanceiro.backend.repositories.MovementRepository;
import br.com.controlefinanceiro.backend.requests.MovementPostRequestBody;
import br.com.controlefinanceiro.backend.services.MovementGroupService;

@Service
public class MovementGroupServiceImpl implements MovementGroupService {

	@Autowired private CategoryRepository categoryRepository;
	@Autowired private MovementGroupRepository movementGroupRepository;
	@Autowired private MovementRepository movementRepository;
	
	@Override
	public MovementGroupModel createNewMovement(MovementPostRequestBody body) {
		var group = new MovementGroupModel();
		
		group.setName(body.getName());
		group.setCategory(categoryRepository.findById(body.getCategoryId()).get());
		group.setFirstDue(body.getDueAt());
		
		group.setFixed(body.isFixed());
		group.setFrequency(body.getFrequency());
		group.setInstallments(body.getInstallments());
		
		group.setCreatedBy(body.getUserId());
		group.setUpdatedBy(body.getUserId());
		
		group.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		group.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		
		
		group = movementGroupRepository.save(group);
		
		if(!group.isFixed()) {
			for (int i = 0; i < body.getInstallments(); i++) {
				var movement = new MovementModel();
				movement.setGroup(group);
				movement.setAmount(body.getAmount());
				
				
				if(i == 0) {
					movement.setDueAt(body.getDueAt());
				} else {
					LocalDate dueAt = null;
					switch (body.getFrequency()) {
					case DAILY: 	dueAt = body.getDueAt().plusDays(i);
					break;
					case MONTHLY: 	dueAt = body.getDueAt().plusMonths(i);
					break;
					case WEEKLY: 	dueAt = body.getDueAt().plusWeeks(i);
					break;
					case YEARLY: 	dueAt = body.getDueAt().plusYears(i);
					break;
					default: throw new RuntimeException("Frequency not implemented");
					}
					movement.setDueAt(dueAt);					
				}
				
				movement.setInstallment(i + 1);
				movement.setName(group.getName());
				movement.setCreatedBy(body.getUserId());
				movement.setUpdatedBy(body.getUserId());
				
				movement.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
				movement.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
				group.getMovements().add(movementRepository.save(movement));
			}
		} else {
			var movement = new MovementModel();
			movement.setGroup(group);
			movement.setAmount(body.getAmount());
			movement.setName(group.getName());
			movement.setDueAt(body.getDueAt());
			
			movement.setCreatedBy(body.getUserId());
			movement.setUpdatedBy(body.getUserId());
			
			movement.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
			movement.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
			group.getMovements().add(movementRepository.save(movement));
		}
		
		
		
		return group;
	}

	@Override
	@Transactional
	public void delete(UUID movementGroupId) {
		MovementGroupModel movementGroup = movementGroupRepository.findById(movementGroupId).orElseThrow(() -> new NotFoundException("Movement Group not found by id: " + movementGroupId));
		movementGroup.getMovements().forEach(item -> movementRepository.delete(item));
		movementGroupRepository.delete(movementGroup);
	}

}
