package br.com.controlefinanceiro.backend.services;

import br.com.controlefinanceiro.backend.models.RoleModel;

public interface RoleService {

	RoleModel findUserRoleOrThrowServerError();
	RoleModel save(RoleModel roleModel);
	
}
