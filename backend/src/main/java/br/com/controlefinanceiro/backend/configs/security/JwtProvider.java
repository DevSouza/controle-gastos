package br.com.controlefinanceiro.backend.configs.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//@Log4j2
@Component
public class JwtProvider {
	
	private static final String AUD_ACCESS_TOKEN = "AccessToken.API";
	private static final String AUD_REFRESH_TOKEN = "RefreshToken.API";
	
	@Value("${kraken.auth.jwtSecret}")
	private String jwtSecret;
	
	@Value("${kraken.auth.jwtAccessTokenExpirationMs}")
	private int jwtAccessTokenExpirationMs;
	
	@Value("${kraken.auth.jwtRefreshTokenExpirationMs}")
	private int jwtRefreshTokenExpirationMs;
	
	public String generateAccessToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts
				.builder()
				.setAudience(AUD_ACCESS_TOKEN)
				.setId(UUID.randomUUID().toString())

				.setSubject(userPrincipal.getUserId().toString())
				.claim("username", userPrincipal.getUsername())
				.claim("email", userPrincipal.getEmail())
				
				.setIssuer("http://localhost/")
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date().getTime() + jwtAccessTokenExpirationMs)))
				
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
				.compact();
	}
	
	public String generateRefreshToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts
				.builder()
				.setAudience(AUD_REFRESH_TOKEN)
				.setSubject(userPrincipal.getUserId().toString())
				
				.setIssuer("http://localhost/")
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date().getTime() + jwtRefreshTokenExpirationMs)))
				
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
				.compact();
	}
	
	public String getSubjectJwt(String token) {
		return Jwts
				.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
					.build()
						.parseClaimsJws(token)
						.getBody()
							.getSubject();
	}
	
	public void validateAccessToken(String token) {
		Jws<Claims> parseClaimsJws = Jwts
			.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
					.build()
						.parseClaimsJws(token);
		
		if(!parseClaimsJws.getBody().getAudience().equals(AUD_ACCESS_TOKEN))
			throw new BadCredentialsException("Bad Credentials Exception");
		
	}
	
	public void validateRefreshToken(String token) {
		Jws<Claims> parseClaimsJws = Jwts
			.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
					.build()
						.parseClaimsJws(token);
		
		if(!parseClaimsJws.getBody().getAudience().equals(AUD_REFRESH_TOKEN))
			throw new BadCredentialsException("Bad Credentials Exception");		
	}
}
