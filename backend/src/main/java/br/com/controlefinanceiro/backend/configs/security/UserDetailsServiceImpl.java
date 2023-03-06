package br.com.controlefinanceiro.backend.configs.security;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.controlefinanceiro.backend.configs.security.services.LoginAttemptService;
import br.com.controlefinanceiro.backend.exceptions.AuthenticationBlockedException;
import br.com.controlefinanceiro.backend.models.UserModel;
import br.com.controlefinanceiro.backend.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired private UserRepository userRepository;
	@Autowired private LoginAttemptService loginAttemptService; 
    @Autowired private HttpServletRequest request;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new AuthenticationBlockedException("blocked");
        }
		
		UserModel userModel = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		
		return UserDetailsImpl.build(userModel);
	}
	
	public UserDetails loadUserByUserId(UUID userId) throws UsernameNotFoundException {
		UserModel userModel = userRepository.findById(userId)
				.orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User Not Found with userId: " + userId));
		
		return UserDetailsImpl.build(userModel);
	}
	
	private String getClientIP() {
	    String xfHeader = request.getHeader("X-Forwarded-For");
	    if (xfHeader == null){
	        return request.getRemoteAddr();
	    }
	    return xfHeader.split(",")[0];
	}

}
