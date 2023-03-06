package br.com.controlefinanceiro.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controlefinanceiro.backend.models.RoleModel;
import br.com.controlefinanceiro.backend.repositories.RoleRepository;
import br.com.controlefinanceiro.backend.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired private RoleRepository roleRepository;

	@Override
	public RoleModel save(RoleModel roleModel) {
		return roleRepository.save(roleModel);
	}

	@Override
	public RoleModel findUserRoleOrThrowServerError() {
		return roleRepository.findByName("User")
				.orElseThrow(() -> new RuntimeException("Role User not found in Database"));
	}
	
}
