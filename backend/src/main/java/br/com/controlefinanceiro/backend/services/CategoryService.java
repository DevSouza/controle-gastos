package br.com.controlefinanceiro.backend.services;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import br.com.controlefinanceiro.backend.models.CategoryModel;
import br.com.controlefinanceiro.backend.requests.CategoryPostRequestBody;
import br.com.controlefinanceiro.backend.requests.CategoryPutRequestBody;
import br.com.controlefinanceiro.backend.response.CategoryCheckDependentGetResponseBody;

public interface CategoryService {

	CategoryModel create(CategoryPostRequestBody body);
	CategoryModel update(UUID categoryId, CategoryPutRequestBody body);

	List<CategoryModel> findAllByUserId(UUID currentUserId);

	void delete(@Valid UUID categoryId, UUID toCategoryId);
	CategoryCheckDependentGetResponseBody checkDependent(@Valid UUID categoryId);

}
