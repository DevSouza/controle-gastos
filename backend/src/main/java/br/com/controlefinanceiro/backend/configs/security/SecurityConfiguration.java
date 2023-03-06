package br.com.controlefinanceiro.backend.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.controlefinanceiro.backend.configs.handler.FilterChainExceptionHandler;
import br.com.controlefinanceiro.backend.configs.handler.ResolverAccessDeniedHandler;

@Configuration
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired private ResolverAccessDeniedHandler resolverAccessDeniedHandler;
	@Autowired private FilterChainExceptionHandler filterChainExceptionHandler;
	
	//@Autowired
	//AuthenticationEntryPointImpl authenticationEntryPoint;
	
	private static final String[] AUTH_WHITELIST = {
		"/auth/**",
		
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/api-docs/**",
		"/v3/api-docs/**",
	};
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .cors().and()
        	.exceptionHandling()
        		.accessDeniedHandler(resolverAccessDeniedHandler)
        		//.authenticationEntryPoint(authenticationEntryPoint)
        	.and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
            .authorizeHttpRequests((authz) -> 
            	authz
            		.antMatchers(AUTH_WHITELIST).permitAll()
                	.anyRequest().authenticated()
            )
            .csrf().disable();
        
        http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandler, ChannelProcessingFilter.class);
        return http.build();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationJwtFilter authenticationJwtFilter() {
		return new AuthenticationJwtFilter();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
