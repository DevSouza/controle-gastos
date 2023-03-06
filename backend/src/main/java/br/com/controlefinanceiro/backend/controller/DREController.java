package br.com.controlefinanceiro.backend.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlefinanceiro.backend.configs.security.services.AuthenticationCurrentUserService;
import br.com.controlefinanceiro.backend.repositories.DRERepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users/{userId}/dre")
@Tag(name="movements")
public class DREController {

	@Autowired AuthenticationCurrentUserService authenticationCurrentUserService;
	@Autowired DRERepository dreRepository;	
	
	@GetMapping
	@Operation(summary = "Find by user", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> findByUser(@PathVariable(value = "userId") UUID userId, @RequestParam(required = false) String year) {
		if(StringUtils.isBlank(year)) year = String.valueOf(LocalDate.now().getYear());
		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!userId.equals(currentUserId))
			throw new AccessDeniedException("Forbidden");
		
		return ResponseEntity.ok(dreRepository.findByYearAndUserId(year, userId));
	}
	
}
