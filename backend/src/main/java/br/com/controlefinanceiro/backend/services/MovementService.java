package br.com.controlefinanceiro.backend.services;

import java.util.UUID;

import javax.validation.Valid;

import br.com.controlefinanceiro.backend.requests.MovementDeleteRequestBody;
import br.com.controlefinanceiro.backend.response.MovementCheckDependentGetResponseBody;

public interface MovementService {
	
	void pay(UUID movementId, UUID currentUserId);
	void cancelPay(UUID movementId, UUID currentUserId);
	MovementCheckDependentGetResponseBody checkDependent(@Valid UUID movementId);
	void delete(@Valid UUID movementId, MovementDeleteRequestBody body);
}
