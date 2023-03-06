package br.com.controlefinanceiro.backend.event;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.controlefinanceiro.backend.enuns.PermissionContext;
import br.com.controlefinanceiro.backend.models.PermissionContextModel;
import br.com.controlefinanceiro.backend.models.PermissionModel;
import br.com.controlefinanceiro.backend.models.RoleModel;
import br.com.controlefinanceiro.backend.models.ServiceModel;
import br.com.controlefinanceiro.backend.repositories.PermissionContextRepository;
import br.com.controlefinanceiro.backend.repositories.PermissionRepository;
import br.com.controlefinanceiro.backend.repositories.RoleRepository;
import br.com.controlefinanceiro.backend.repositories.ServiceRepository;

//@Log4j2
@Component
public class ApplicationReadyEventListener {

	@Autowired private ServiceRepository serviceRepository;
	@Autowired private PermissionRepository permissionRepository;
	@Autowired private PermissionContextRepository permissionContextRepository;
	@Autowired private RoleRepository roleRepository;
	
	@EventListener
	public void appReady(ApplicationReadyEvent event) {
		
		ServiceModel sAuthentication = saveService("kraken-authentication", "Authentication Service");		

		var p1 = savePermission(sAuthentication, "user:update", "Atualizar Perfil do usuário", "Atualizar Perfil do usuário");
		var p2 = savePermission(sAuthentication, "user:update:password", "Atualizar Senha do Usuário", "Atualizar Senha do Usuário");
		
		var pc1 = insertPermissionContext(PermissionContext.APPLICATION, p1);
		var pc2 = insertPermissionContext(PermissionContext.APPLICATION, p2);

		insertRole("User", "User application", new HashSet<PermissionContextModel>(Arrays.asList(pc1, pc2)));
	}
	
	private ServiceModel saveService(String name, String description) {
		Optional<ServiceModel> opt = serviceRepository.findByName(name);		
		if(opt.isPresent()) return opt.get();
		
		ServiceModel service = ServiceModel.builder()			
				.name(name)
				.description(description)
				.createdAt(LocalDateTime.now(ZoneId.of("UTC")))
				.updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
				.build();
		
		return serviceRepository.save(service);
	}
	
	private PermissionModel savePermission(ServiceModel service, String code, String name, String description) {
		Optional<PermissionModel> opt = permissionRepository.findByName(name);		
		if(opt.isPresent()) return opt.get();
		
		PermissionModel permission = PermissionModel.builder()
				.service(service)
				.code(code)
				.name(name)
				.description(description)
				.createdAt(LocalDateTime.now(ZoneId.of("UTC")))
				.updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
				.build();
		
		return permissionRepository.save(permission);
	}

	private RoleModel insertRole(String name, String description, HashSet<PermissionContextModel> permissions) {
		Optional<RoleModel> opt = roleRepository.findByName(name);		
		if(opt.isPresent()) return opt.get();
		
		RoleModel role = RoleModel.builder()
				.name(name)
				.description(description)
				.permissions(permissions)
				.createdAt(LocalDateTime.now(ZoneId.of("UTC")))
				.updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
				.build();
		
		return roleRepository.save(role);
	}
	
	private PermissionContextModel insertPermissionContext(PermissionContext context, PermissionModel permission) {
		Optional<PermissionContextModel> opt = permissionContextRepository.findByContextAndPermission(context, permission);
		if(opt.isPresent()) return opt.get();
		
		PermissionContextModel obj = PermissionContextModel.builder()
					.context(context)
					.permission(permission)
					.createdAt(LocalDateTime.now(ZoneId.of("UTC")))
					.updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
					.build();
			
		return permissionContextRepository.save(obj);
			
	}
	
}
