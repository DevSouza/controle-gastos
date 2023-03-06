package br.com.controlefinanceiro.backend.services.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.controlefinanceiro.backend.configs.security.services.AuthenticationCurrentUserService;
import br.com.controlefinanceiro.backend.dtos.MovementDTO;
import br.com.controlefinanceiro.backend.exceptions.NotFoundException;
import br.com.controlefinanceiro.backend.models.MovementModel;
import br.com.controlefinanceiro.backend.repositories.MovementRepository;
import br.com.controlefinanceiro.backend.requests.MovementDeleteRequestBody;
import br.com.controlefinanceiro.backend.response.MovementCheckDependentGetResponseBody;
import br.com.controlefinanceiro.backend.services.MovementGroupService;
import br.com.controlefinanceiro.backend.services.MovementService;

@Service
public class MovementServiceImpl implements Serializable, MovementService {
	private static final long serialVersionUID = 1L;

	@Autowired AuthenticationCurrentUserService authenticationCurrentUserService;
	@Autowired private MovementRepository movementRepository;
	@Autowired private MovementGroupService movementGroupService;
	
	@Override
	public void pay(UUID movementId, UUID currentUserId) {
		Optional<MovementModel> movementOpt = movementRepository.findById(movementId);
		
		MovementModel movement = movementOpt.get();
		if(!movement.getCreatedBy().equals(currentUserId)) throw new AccessDeniedException("Forbidden");
		
		movement.setPaidAt(LocalDateTime.now(ZoneId.of("UTC")));
		movement.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		movement.setUpdatedBy(currentUserId);
		
		movementRepository.save(movement);
	}

	@Override
	public void cancelPay(UUID movementId, UUID currentUserId) {
		Optional<MovementModel> movementOpt = movementRepository.findById(movementId);
		
		MovementModel movement = movementOpt.get();
		if(!movement.getCreatedBy().equals(currentUserId)) throw new AccessDeniedException("Forbidden");
		
		movement.setPaidAt(null);
		movement.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		movement.setUpdatedBy(currentUserId);
		
		movementRepository.save(movement);
	}

	@Override
	public MovementCheckDependentGetResponseBody checkDependent(@Valid UUID movementId) {
		MovementModel movement = movementRepository.findById(movementId).orElseThrow(() -> new NotFoundException("Movement not found by id: " + movementId));
		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!movement.getCreatedBy().equals(currentUserId))
			 throw new AccessDeniedException("Forbidden");
		
		List<MovementModel> movements = movement.getGroup().getMovements();
		
		var result = new MovementCheckDependentGetResponseBody();

		if(movements != null && movements.size() > 1) {
			result.setHasDependents(true);
			result.setMovements(movements.stream().map(item -> item.toMovementDTO()).collect(Collectors.toList()));
			result.getMovements().sort(new Comparator<MovementDTO>() {
				@Override
				public int compare(MovementDTO o1, MovementDTO o2) {
					return o1.getDueAt().compareTo(o2.getDueAt());
				}
			});
			return result;
		}
		
		return result;
	}

	@Override
	@Transactional
	public void delete(@Valid UUID movementId, MovementDeleteRequestBody body) {
		MovementModel movement = movementRepository.findById(movementId).orElseThrow(() -> new NotFoundException("Movement not found by id: " + movementId));
		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!movement.getCreatedBy().equals(currentUserId))
			 throw new AccessDeniedException("Forbidden");
		
		switch (body.getType()) {
		case "THIS":
			if(movement.getGroup().getMovements().size() > 1) 
				movementRepository.delete(movement);
			else 
				movementGroupService.delete(movement.getGroup().getMovementGroupId());
			
			break;
		case "ALL":
			movementGroupService.delete(movement.getGroup().getMovementGroupId());
			break;
		default:
			throw new IllegalArgumentException("Type not implements");
		}
		
	}
	
}
