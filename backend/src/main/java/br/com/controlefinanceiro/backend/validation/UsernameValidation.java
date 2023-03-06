package br.com.controlefinanceiro.backend.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameValidation {
	//error message
    public String message() default "Username não disponível. ";
    
    //represents group of constraints
    public Class<?>[] groups() default {};
    
    //represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};
}
