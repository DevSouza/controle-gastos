package br.com.controlefinanceiro.backend.configs.handler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.controlefinanceiro.backend.exceptions.AuthenticationBlockedException;
import br.com.controlefinanceiro.backend.exceptions.BadRequestException;
import br.com.controlefinanceiro.backend.exceptions.BadRequestExceptionDetails;
import br.com.controlefinanceiro.backend.exceptions.ConflictException;
import br.com.controlefinanceiro.backend.exceptions.ExceptionDetails;
import br.com.controlefinanceiro.backend.exceptions.NotFoundException;
import br.com.controlefinanceiro.backend.exceptions.ValidationError;
import br.com.controlefinanceiro.backend.exceptions.ValidationExceptionDetails;
import io.jsonwebtoken.ExpiredJwtException;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException e) {
		BadRequestExceptionDetails body = BadRequestExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Bad Request Exception, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.inner(e.getInner())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionDetails> handleNotFoundException(NotFoundException e) {
		ExceptionDetails body = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Not Found Exception, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<BadRequestExceptionDetails> handleAccessDeniedException(AuthenticationCredentialsNotFoundException e) {
		BadRequestExceptionDetails body = BadRequestExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("User not found, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<BadRequestExceptionDetails> handleAccessDeniedException(AccessDeniedException e) {
		BadRequestExceptionDetails body = BadRequestExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Access Denied Exception, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<BadRequestExceptionDetails> handleExpiredJwtException(ExpiredJwtException e) {
		BadRequestExceptionDetails body = BadRequestExceptionDetails.builder()
				.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
				.title("Bad Request Exception, Check the Documentation")
				.details(e.getMessage())
				.developerMessage(e.getClass().getName())
				.code("token.expired")
				.build();
		
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionDetails> handleBadCredentialsException(BadCredentialsException e) {
		ExceptionDetails body = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Bad Credentials Exception, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	
	
	
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<?> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
		if(e.getCause() instanceof NotFoundException) {
			return handleNotFoundException((NotFoundException) e.getCause());
		} else if(e.getCause() instanceof AuthenticationBlockedException) {
			return handleAuthenticationBlockedException((AuthenticationBlockedException) e.getCause());
		}
		
		ExceptionDetails body = ExceptionDetails.builder()
				.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
				.title("Internal Authentication Service Exception, Check the Documentation")
				.details(e.getMessage())
				.developerMessage(e.getClass().getName())
				.build();
			
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(value = DataIntegrityViolationException.class)
	public ResponseEntity<?> handleBadRequestException(DataIntegrityViolationException e) {
		ExceptionDetails body = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Data Integrity Violation Exception, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = AuthenticationBlockedException.class)
	public ResponseEntity<ExceptionDetails> handleAuthenticationBlockedException(AuthenticationBlockedException e) {
		ExceptionDetails body = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("User blocked for making numerous failed login attempts")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.code("blocked")
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.I_AM_A_TEAPOT);
	}
	
	@ExceptionHandler(value = ConflictException.class)
	public ResponseEntity<ExceptionDetails> handleConflictException(ConflictException e) {
		ExceptionDetails body = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("ConflictException")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ExceptionDetails> handleBadRequestException(Exception e) {
		ExceptionDetails body = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Exception, Check the Documentation")
			.details(e.getMessage())
			.developerMessage(e.getClass().getName())
			.build();
		
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
		String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining(", "));

		// YUP React
		List<ValidationError> inner = new ArrayList<ValidationError>();
		fieldErrors.forEach(item -> {
			inner.add(new ValidationError(item.getRejectedValue(), item.getField(), item.getDefaultMessage()));
		});

		ValidationExceptionDetails validationExceptionDetails = ValidationExceptionDetails.builder()
			.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
			.title("Bad Request Exception, Invalid Fields")
			.details("Check the field(s) error")
			.developerMessage(exception.getClass().getName()).fields(fields).fieldsMessage(fieldsMessage)
			.inner(inner)
			.build();
		
		return new ResponseEntity<>(validationExceptionDetails, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ExceptionDetails exceptionDetails = ExceptionDetails.builder()
				.timestamp(LocalDateTime.now(ZoneId.of("UTC")))
				.title(ex.getCause().getMessage())
				.details(ex.getMessage())
				.developerMessage(ex.getClass().getName())
				.build();

		return new ResponseEntity<>(exceptionDetails, headers, status);
	}
	
}
