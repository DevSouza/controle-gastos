package br.com.controlefinanceiro.backend.services;

import java.util.UUID;

import br.com.controlefinanceiro.backend.models.MovementGroupModel;
import br.com.controlefinanceiro.backend.requests.MovementPostRequestBody;

public interface MovementGroupService {
	
	MovementGroupModel createNewMovement(MovementPostRequestBody body);
	void delete(UUID movementGroupId);
}
