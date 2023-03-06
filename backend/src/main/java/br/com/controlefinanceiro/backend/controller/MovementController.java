package br.com.controlefinanceiro.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlefinanceiro.backend.configs.security.services.AuthenticationCurrentUserService;
import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import br.com.controlefinanceiro.backend.exceptions.NotFoundException;
import br.com.controlefinanceiro.backend.models.MovementModel;
import br.com.controlefinanceiro.backend.repositories.MovementRepository;
import br.com.controlefinanceiro.backend.requests.MovementDeleteRequestBody;
import br.com.controlefinanceiro.backend.requests.MovementPostRequestBody;
import br.com.controlefinanceiro.backend.response.MovementGetResponseBody;
import br.com.controlefinanceiro.backend.response.MovementSummaryGetResponseBody;
import br.com.controlefinanceiro.backend.services.MovementGroupService;
import br.com.controlefinanceiro.backend.services.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/movements")
@Tag(name="movements")
public class MovementController {

	@Autowired AuthenticationCurrentUserService authenticationCurrentUserService;
	@Autowired MovementGroupService movementGroupService;
	@Autowired MovementService movementService;
	@Autowired MovementRepository movementRepository;
	
	@PostMapping
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid Fields", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to edit.", content = @Content),
	})
	@Operation(summary = "Create Movement", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> createMovement(@RequestBody @Valid MovementPostRequestBody body) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(body.getUserId())) throw new AccessDeniedException("Forbidden");
        
		return ResponseEntity.ok(movementGroupService.createNewMovement(body));			
	}
	
	@PatchMapping("/{movementId}/pay")
	@Operation(summary = "Pay movement", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> pay(@PathVariable String movementId) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		movementService.pay(UUID.fromString(movementId), currentUserId);
		
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/{movementId}/cancel-pay")
	@Operation(summary = "Cancel pay movement", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> cancelPay(@PathVariable String movementId) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		movementService.cancelPay(UUID.fromString(movementId), currentUserId);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	@Operation(summary = "Find All by current User", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> findAll(@RequestParam
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		List<MovementModel> movements = movementRepository.findByDueAtBetweenAndUserId(start, end, currentUserId);
		
		return ResponseEntity.ok(MovementGetResponseBody.toMovementGetResponseBody(movements));
	}
	
	@GetMapping("/{movementId}")
	@Operation(summary = "Find by movementId", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> findAll(@PathVariable String movementId) {
        
		MovementModel movement = movementRepository.findById(UUID.fromString(movementId))
				.orElseThrow(() -> new NotFoundException("Movement not found with id " + movementId));
				
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(movement.getCreatedBy())) throw new AccessDeniedException("Forbidden");
		
		return ResponseEntity.ok(movement.toMovementDTO());
	}
	
	@GetMapping("/summary")
	@Operation(summary = "Summary by current User", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> getSummary() {		
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		
		LocalDate start = LocalDate.now();
		start = start.minusDays(start.getDayOfMonth()-1);
		
		LocalDate end = LocalDate.now();
		end = end.plusDays(end.getMonth().maxLength()-end.getDayOfMonth());
		
		List<MovementModel> movements = movementRepository.findByDueAtBetweenAndUserId(start, end, currentUserId);
		MovementSummaryGetResponseBody summary = MovementSummaryGetResponseBody.builder()
			.end(end)
			.start(start)
			.expense(BigDecimal.ZERO)
			.revenue(BigDecimal.ZERO)
			.result(BigDecimal.ZERO)
			.date(LocalDate.now())
			.build();
		
		for (MovementModel movement : movements) {
			if(movement.getGroup().getCategory().getType().equals(TypeCategory.EXPENSE)) {
				summary.setExpense(summary.getExpense().add(movement.getAmount()));
				summary.setResult(summary.getResult().subtract(movement.getAmount()));
			} else {
				summary.setRevenue(summary.getRevenue().add(movement.getAmount()));
				summary.setResult(summary.getResult().add(movement.getAmount()));
			}
			
		}
		
		return ResponseEntity.ok(
			summary
		);
	}
	
	@DeleteMapping("/{movementId}")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid Fields", content = @Content),
		@ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to edit.", content = @Content),
	})
	@Operation(summary = "Delete Movement", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> createCategory(@PathVariable @Valid UUID movementId, @RequestBody MovementDeleteRequestBody body) {	
		movementService.delete(movementId, body);
		return ResponseEntity.ok().build();			
	}
	
	
	@GetMapping("/{movementId}/check-dependent")
	@Operation(summary = "Check dependent to delete", security = {@SecurityRequirement(name = "AccessToken")})
	public ResponseEntity<?> checkDependent(@PathVariable @Valid UUID movementId) {		
		return ResponseEntity.ok(movementService.checkDependent(movementId));
	}
}
