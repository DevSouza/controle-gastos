package br.com.controlefinanceiro.backend.configs.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ResolverAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	@Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.debug("ResolverAccessDeniedHandler {}", accessDeniedException.getClass());
		
		resolver.resolveException(request, response, null, accessDeniedException);
	}

}
