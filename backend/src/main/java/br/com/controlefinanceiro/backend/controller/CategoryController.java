package br.com.controlefinanceiro.backend.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlefinanceiro.backend.configs.security.services.AuthenticationCurrentUserService;
import br.com.controlefinanceiro.backend.requests.CategoryDeleteRequestBody;
import br.com.controlefinanceiro.backend.requests.CategoryPostRequestBody;
import br.com.controlefinanceiro.backend.requests.CategoryPutRequestBody;
import br.com.controlefinanceiro.backend.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categories")
@Tag(name="categories")
public class CategoryController {

	@Autowired AuthenticationCurrentUserService authenticationCurrentUserService;
	@Autowired CategoryService categoryService;
	
	@PostMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid Fields", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to edit.", content = @Content)
	})
	@Operation(summary = "Create Category", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryPostRequestBody body) {		
		return ResponseEntity.ok(categoryService.create(body));			
	}
	
	@PutMapping("/{categoryId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid Fields", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to edit.", content = @Content)
	})
	@Operation(summary = "Update Category", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> createCategory(@PathVariable @Valid UUID categoryId, @RequestBody @Valid CategoryPutRequestBody body) {
		return ResponseEntity.ok(categoryService.update(categoryId,body));			
	}
	
	@DeleteMapping("/{categoryId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid Fields", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to edit.", content = @Content),
		@ApiResponse(responseCode = "409", description = "Conflict: Category Type does not match.", content = @Content),
	})
	@Operation(summary = "Delete Category", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> createCategory(@PathVariable @Valid UUID categoryId, @RequestBody CategoryDeleteRequestBody body) {	
		categoryService.delete(categoryId, body.getToCategoryId());
		return ResponseEntity.ok().build();			
	}
	
	
	@GetMapping
	@Operation(summary = "Find All by current User", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> findAll() {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		return ResponseEntity.ok(categoryService.findAllByUserId(currentUserId));
	}
	
	@GetMapping("/{categoryId}/check-dependent")
	@Operation(summary = "Check dependent to delete", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> checkDependent(@PathVariable @Valid UUID categoryId) {		
		return ResponseEntity.ok(categoryService.checkDependent(categoryId));
	}
}
