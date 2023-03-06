package br.com.controlefinanceiro.backend.configs.security.listeners;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import br.com.controlefinanceiro.backend.configs.security.services.LoginAttemptService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	
	@Autowired private HttpServletRequest request;
	@Autowired private LoginAttemptService loginAttemptService;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			loginAttemptService.loginFailed(request.getRemoteAddr());
			log.info(request.getRemoteAddr());
		} else {
			loginAttemptService.loginFailed(xfHeader.split(",")[0]);
		}
	}
}
