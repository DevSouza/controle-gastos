package br.com.controlefinanceiro.backend.services;

import java.util.Optional;
import java.util.UUID;

import br.com.controlefinanceiro.backend.exceptions.NotFoundException;
import br.com.controlefinanceiro.backend.models.UserModel;
import br.com.controlefinanceiro.backend.requests.ForgotPasswordRequestBody;
import br.com.controlefinanceiro.backend.requests.SignUpPostRequestBody;
import br.com.controlefinanceiro.backend.requests.UserPatchPasswordRequestBody;
import br.com.controlefinanceiro.backend.requests.UserPutRequestBody;

public interface UserService {

	Optional<UserModel> findById(UUID userId);
	Optional<UserModel> findByUsername(String username);
	Optional<UserModel> findByEmail(String email);

	
	
    UserModel save(UserModel userModel);
    UserModel signUpUser(SignUpPostRequestBody body);
    UserModel updateUser(UUID userId, UserPutRequestBody body);
    void updatePassword(UUID userId, UserPatchPasswordRequestBody body);
    void recoveryPassword(ForgotPasswordRequestBody body);
    
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	

	default UserModel findByIdOrThrowNotFoundException(UUID userId) {
		return findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found"));
	};
	default UserModel findByEmailOrThrowNotFoundException(String email){
		return findByEmail(email)
				.orElseThrow(() -> new NotFoundException("User not found with email + " + email));
	};
}
