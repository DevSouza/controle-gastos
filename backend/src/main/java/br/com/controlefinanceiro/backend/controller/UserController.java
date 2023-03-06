package br.com.controlefinanceiro.backend.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlefinanceiro.backend.configs.security.services.AuthenticationCurrentUserService;
import br.com.controlefinanceiro.backend.requests.UserPatchPasswordRequestBody;
import br.com.controlefinanceiro.backend.requests.UserPutRequestBody;
import br.com.controlefinanceiro.backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name="users")
public class UserController {

	@Autowired AuthenticationCurrentUserService authenticationCurrentUserService;
	@Autowired UserService userService;
	
	@GetMapping("/{userId}")
	@Operation(summary = "Find one user", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> getOneUser(@PathVariable(value = "userId") UUID userId) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(userId)) throw new AccessDeniedException("Forbidden");
		
		return ResponseEntity.ok(userService.findByIdOrThrowNotFoundException(userId));
	}
	
	@PatchMapping("/{userId}/password")
	@Operation(summary = "Update password", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> updatePassword(@PathVariable(value = "userId") UUID userId, @RequestBody @Valid UserPatchPasswordRequestBody body) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(userId)) throw new AccessDeniedException("Forbidden");
		
		userService.updatePassword(userId, body);
		
		return ResponseEntity.status(HttpStatus.OK).build();			
	}
	
	@PutMapping("/{userId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid Fields", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to edit this user.", content = @Content),
		@ApiResponse(responseCode = "404", description = "User not found", content = @Content)
	})
	@Operation(summary = "Update an existing user", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> updateUser(@PathVariable(value = "userId") UUID userId, @RequestBody @Valid UserPutRequestBody userPutRequestBody) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(userId)) throw new AccessDeniedException("Forbidden");
        
		return ResponseEntity.ok(userService.updateUser(userId, userPutRequestBody));			
	}
	
}
