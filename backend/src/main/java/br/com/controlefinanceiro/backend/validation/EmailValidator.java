package br.com.controlefinanceiro.backend.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.controlefinanceiro.backend.services.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmailValidator implements ConstraintValidator<EmailValidation, String> {

	@Autowired UserService userService;
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		log.debug("Validation email {}", email);
		
		if(userService.existsByEmail(email)) {
			log.warn("Email {} is Already taken", email);  		
			return false;
		}
		return true;
	}

}
