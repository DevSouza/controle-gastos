package br.com.controlefinanceiro.backend.configs.security;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

//@Log4j2
public class AuthenticationJwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if(requestURI.contains("/auth/signin") || requestURI.contains("/auth/refreshToken")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String jwtStr = getTokenHeader(request);
		if(jwtStr != null) {
			jwtProvider.validateAccessToken(jwtStr);
			
			String userId = jwtProvider.getSubjectJwt(jwtStr);
			UserDetails userDetails = userDetailsService.loadUserByUserId(UUID.fromString(userId));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
			
	}
	
	private String getTokenHeader(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if(StringUtils.hasText(headerAuth) && (headerAuth.startsWith("Bearer ") || headerAuth.startsWith("bearer "))) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}
	
}
