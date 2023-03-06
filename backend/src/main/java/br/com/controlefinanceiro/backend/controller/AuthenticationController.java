package br.com.controlefinanceiro.backend.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controlefinanceiro.backend.configs.security.JwtProvider;
import br.com.controlefinanceiro.backend.configs.security.UserDetailsServiceImpl;
import br.com.controlefinanceiro.backend.dtos.JwtDto;
import br.com.controlefinanceiro.backend.dtos.LoginDto;
import br.com.controlefinanceiro.backend.dtos.RefreshTokenDto;
import br.com.controlefinanceiro.backend.requests.ForgotPasswordRequestBody;
import br.com.controlefinanceiro.backend.requests.SignUpPostRequestBody;
import br.com.controlefinanceiro.backend.services.RoleService;
import br.com.controlefinanceiro.backend.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/auth")
@Tag(name = "auth")
public class AuthenticationController {

    @Autowired UserService userService;
    @Autowired RoleService roleService;
    
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserDetailsServiceImpl userDetailsService;
    @Autowired JwtProvider jwtProvider;
    
    @Value("${kraken.auth.tokenType}")
	private String tokenType;

    @Operation(summary = "Sign up in the platform")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpPostRequestBody body) {
    	log.debug("POST signUp SignUpPostRequestBody received {} ", body.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUpUser(body));
    }
    
    @Operation(summary = "Sign in the platform")
    @PostMapping("/signin")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
    	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
    	SecurityContextHolder.getContext().setAuthentication(authentication);    	
    	String atJWT = jwtProvider.generateAccessToken(authentication);
    	String rtJWT = jwtProvider.generateRefreshToken(authentication);
    	
    	return ResponseEntity.ok(new JwtDto(atJWT, rtJWT, tokenType));
    }
    
    @Operation(summary = "Obtain additional access tokens")
    @PostMapping("/refreshToken")
    public ResponseEntity<JwtDto> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
    	if(refreshTokenDto.getRefreshToken() != null) {
    		try {
    			jwtProvider.validateRefreshToken(refreshTokenDto.getRefreshToken());
				
    			String userId = jwtProvider.getSubjectJwt(refreshTokenDto.getRefreshToken());
    			UserDetails userDetails = userDetailsService.loadUserByUserId(UUID.fromString(userId));
    			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    			String atJWT = jwtProvider.generateAccessToken(authentication);
    			return ResponseEntity.ok(new JwtDto(atJWT, tokenType));
			} catch (ExpiredJwtException e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
    		
		}
    	
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @Operation(summary = "Send Email with a temporary Password")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestBody forgotPasswordRequestBody) {
		userService.recoveryPassword(forgotPasswordRequestBody);
    	return ResponseEntity.noContent().build();
    }
    
}
