package br.com.controlefinanceiro.backend.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.controlefinanceiro.backend.services.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UsernameValidator implements ConstraintValidator<UsernameValidation, String> {

	@Autowired UserService userService;
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		log.debug("Validation username {}", username);
		
		if(userService.existsByUsername(username)) {
			log.warn("Username {} is Already taken", username);    		
			return false;
		}
		return true;
	}

}
