package br.com.controlefinanceiro.backend.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.enuns.UserStatus;
import br.com.controlefinanceiro.backend.exceptions.BadRequestException;
import br.com.controlefinanceiro.backend.exceptions.ConflictException;
import br.com.controlefinanceiro.backend.exceptions.ValidationError;
import br.com.controlefinanceiro.backend.models.RoleModel;
import br.com.controlefinanceiro.backend.models.UserModel;
import br.com.controlefinanceiro.backend.repositories.UserRepository;
import br.com.controlefinanceiro.backend.requests.ForgotPasswordRequestBody;
import br.com.controlefinanceiro.backend.requests.SignUpPostRequestBody;
import br.com.controlefinanceiro.backend.requests.UserPatchPasswordRequestBody;
import br.com.controlefinanceiro.backend.requests.UserPutRequestBody;
import br.com.controlefinanceiro.backend.services.EmailService;
import br.com.controlefinanceiro.backend.services.RoleService;
import br.com.controlefinanceiro.backend.services.UserService;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired UserRepository userRepository;
    @Autowired RoleService roleService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EmailService emailService;
    
    
	@Override
	public Optional<UserModel> findById(UUID userId) {
		return userRepository.findById(userId);
	}
	@Override
	public Optional<UserModel> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<UserModel> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserModel save(UserModel userModel) {
		return userRepository.save(userModel);
	}

	@Override
	@Transactional
	public UserModel signUpUser(SignUpPostRequestBody body) {
		RoleModel role = roleService.findUserRoleOrThrowServerError(); // TODO: Alterar Roles para Teams 
        
        var userModel = new UserModel();
        BeanUtils.copyProperties(body, userModel);
        userModel.getUserRole().add(role);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setPassword(passwordEncoder.encode(body.getPassword()));
        userModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        userModel = userRepository.save(userModel);        
        log.debug("POST registerUser userId saved {}", userModel.getUserId());
        log.info("User saved successfully userId {}", userModel.getUserId());

        var userProducer = new UserProducer();
		BeanUtils.copyProperties(userModel, userProducer);
        emailService.sendEmail(userProducer, "wellcome-user.flth", "Bem vindo(a) a plataforma Kraken");
        
        return userModel;
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public void updatePassword(UUID userId, UserPatchPasswordRequestBody body) {
		UserModel user = findByIdOrThrowNotFoundException(userId);
		
		if(!passwordEncoder.matches(body.getOldPassword(), user.getPassword())) {
			log.warn("Mismatched old password userId {} ", userId);			
			throw new ConflictException("Mismatched old password");
		}
		
		user.setPassword(passwordEncoder.encode(body.getPassword()));
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		
		save(user);
		
		var userProducer = new UserProducer();
		BeanUtils.copyProperties(user, userProducer);
        emailService.sendEmail(userProducer, "updated-password.flth", "Senha Atualizada - Plataforma Kraken");
	}

	@Override
	public UserModel updateUser(UUID userId, UserPutRequestBody body) {
		UserModel user = findByIdOrThrowNotFoundException(userId);
		
		List<ValidationError> inner = new ArrayList<>();
		Optional<UserModel> findByEmail = findByEmail(body.getEmail());
		if(findByEmail.isPresent() && !findByEmail.get().getUserId().equals(userId)) {
			log.warn("Email {} is Already taken", body.getEmail());
			inner.add(new ValidationError(body.getEmail(), "email", "Email não disponivel."));
		}
		
		Optional<UserModel> findByUsername = findByUsername(body.getUsername());
		if(findByUsername.isPresent() && !findByUsername.get().getUserId().equals(userId)) {
			log.warn("Username {} is Already taken", body.getUsername());
			inner.add(new ValidationError(body.getUsername(), "username", "Username não disponivel."));
		}
		
		if(!inner.isEmpty()) throw new BadRequestException("Bad Request Exception, Invalid Fields", inner);
		
		BeanUtils.copyProperties(body, user);
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		return save(user);
	}
	
	@Override
	@Transactional
	public void recoveryPassword(ForgotPasswordRequestBody body) {
		UserModel user = findByEmailOrThrowNotFoundException(body.getEmail());
		
		String temporaryPassword = RandomStringUtils.randomAlphanumeric(8);
		
		user.setPassword(passwordEncoder.encode(temporaryPassword));
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		
		user = save(user);
		
		var userProducer = new UserProducer();
		BeanUtils.copyProperties(user, userProducer);
		userProducer.setTemporaryPassword(temporaryPassword);
		
		emailService.sendEmail(userProducer, "recovery-password.flth", "Senha Temporária - Plataforma Kraken");
	}
	
	@Data
	@ToString
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public class UserProducer {
		private UUID userId;
		private String fullName;
		private String email;
		private String temporaryPassword;
	}
	
}
