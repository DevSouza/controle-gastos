package br.com.controlefinanceiro.backend.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.controlefinanceiro.backend.configs.security.services.AuthenticationCurrentUserService;
import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import br.com.controlefinanceiro.backend.exceptions.BadRequestException;
import br.com.controlefinanceiro.backend.exceptions.ConflictException;
import br.com.controlefinanceiro.backend.exceptions.NotFoundException;
import br.com.controlefinanceiro.backend.exceptions.ValidationError;
import br.com.controlefinanceiro.backend.models.CategoryModel;
import br.com.controlefinanceiro.backend.models.MovementGroupModel;
import br.com.controlefinanceiro.backend.repositories.CategoryRepository;
import br.com.controlefinanceiro.backend.repositories.MovementGroupRepository;
import br.com.controlefinanceiro.backend.requests.CategoryPostRequestBody;
import br.com.controlefinanceiro.backend.requests.CategoryPutRequestBody;
import br.com.controlefinanceiro.backend.response.CategoryCheckDependentGetResponseBody;
import br.com.controlefinanceiro.backend.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private MovementGroupRepository movementGroupRepository;
	@Autowired private AuthenticationCurrentUserService authenticationCurrentUserService;
	
	@Override
	public CategoryModel create(CategoryPostRequestBody body) {
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(body.getUserId())) throw new AccessDeniedException("Forbidden");
		
		var category = new CategoryModel();
		BeanUtils.copyProperties(body, category);
		
		category.setUpdatedBy(body.getUserId());
		category.setCreatedBy(body.getUserId());
		
		category.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		category.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));

		return categoryRepository.save(category);
	}

	@Override
	public List<CategoryModel> findAllByUserId(UUID currentUserId) {
		return categoryRepository.findByCreatedBy(currentUserId);
	}

	@Override
	@Transactional
	public void delete(@Valid UUID categoryId, UUID toCategoryId) {
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		CategoryModel category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Categoria não existe"));
		
		if(!currentUserId.equals(category.getCreatedBy())) throw new AccessDeniedException("Forbidden");
		
		List<MovementGroupModel> groups = movementGroupRepository.findByCategoryIdAndUserId(categoryId, currentUserId);
		
		if(groups.size() > 0) {
			if(toCategoryId == null)
				throw new BadRequestException("Migration Category not found", Arrays.asList(new ValidationError(null, "toCategoryId", "O Valor é requerido!")));
			
			CategoryModel toCategory = categoryRepository.findById(toCategoryId).orElseThrow(() -> new NotFoundException("Categoria não existe"));
			if(!currentUserId.equals(toCategory.getCreatedBy())) throw new AccessDeniedException("Forbidden");
			
			if(!category.getType().equals(toCategory.getType()))
				throw new ConflictException("As categorias não são do mesmo tipo");

			groups.forEach(item -> {
				item.setCategory(toCategory);
				item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
				item.setUpdatedBy(currentUserId);
				
				movementGroupRepository.save(item);
			});
		}
		
		categoryRepository.delete(category);
	}

	@Override
	public CategoryModel update(UUID categoryId, CategoryPutRequestBody body) {
		var category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Categoria não existe"));

		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(category.getCreatedBy())) throw new AccessDeniedException("Forbidden");
		
		if(category.getType().equals(TypeCategory.EXPENSE)) {
			category.setLimitMaxPercentage(body.getLimitMaxPercentage());
			category.setLimitMaxValue(body.getLimitMaxValue());
		}
		
		category.setName(body.getName());
		category.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		category.setUpdatedBy(currentUserId);
		
		return categoryRepository.save(category);
	}

	@Override
	public CategoryCheckDependentGetResponseBody checkDependent(@Valid UUID categoryId) {
		var category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Categoria não existe"));

		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(category.getCreatedBy())) throw new AccessDeniedException("Forbidden");
		
		List<MovementGroupModel> groups = movementGroupRepository.findByCategoryIdAndUserId(categoryId, currentUserId);
		var result = new CategoryCheckDependentGetResponseBody();
		result.setNeedMigration(groups.size() > 0);
		return result;
	}
	
}
