package br.com.controlefinanceiro.backend.configs.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.controlefinanceiro.backend.configs.security.UserDetailsImpl;

@Service
public class AuthenticationCurrentUserService {

	public UserDetailsImpl getCurrentUser() {
		return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public Authentication getAuthentication( ) {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
