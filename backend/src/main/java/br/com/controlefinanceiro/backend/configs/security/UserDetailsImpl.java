package br.com.controlefinanceiro.backend.configs.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.controlefinanceiro.backend.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private UUID userId;
	private String fullName;
	private String username;
	
	@JsonIgnore
	private String password;
	private String email;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public static UserDetailsImpl build(UserModel userModel) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		return new UserDetailsImpl(
				userModel.getUserId(),
				userModel.getFullName(),
				userModel.getUsername(),
				userModel.getPassword(),
				userModel.getEmail(),
				authorities);
	}	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
