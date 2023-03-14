package br.com.controlefinanceiro.backend.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.controlefinanceiro.backend.models.CategoryModel;
import lombok.extern.log4j.Log4j2;

@DataJpaTest
@ActiveProfiles()
@DisplayName("Teste para Category Repository")
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Log4j2
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	@DisplayName("Save create category successful")
	void save_CreateCategory_whenSuccessful() {
		var category = createCategory();
		
		var savedCategory = this.categoryRepository.save(category);
		Assertions.assertThat(savedCategory).isNotNull();
		Assertions.assertThat(savedCategory.getCategoryId()).isNotNull();
		Assertions.assertThat(savedCategory.getName()).isEqualTo(category.getName());
		
	}
	
	@Test
	@DisplayName("Save update category successful")
	void save_UpdateCategory_whenSuccessful() {
		var category = createCategory();
		
		var savedCategory = this.categoryRepository.save(category);
		
		savedCategory.setName("Category 321");
		var updatedCategory = this.categoryRepository.save(category);
		
		
		log.info(updatedCategory);
		Assertions.assertThat(updatedCategory).isNotNull();
		Assertions.assertThat(updatedCategory.getCategoryId()).isNotNull();
		Assertions.assertThat(updatedCategory.getName()).isEqualTo(savedCategory.getName());
		
	}
	
	private CategoryModel createCategory() {
		return CategoryModel.builder()
				.name("Categoria 123")
				.build();
	}

}
