package ru.practicum.ewm.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceImplTest {
    private final CategoryService service;

    private Category category1;
    private Category category2;
    private CategoryDto category1Dto;
    private CategoryDto category2Dto;

    private NewCategoryDto category1NewDto;
    private NewCategoryDto category2NewDto;


    @BeforeEach
    public void createData() {
        category1 = new Category(1, "category_name1");
        category2 = new Category(2, "category_name2");
        category1NewDto = new NewCategoryDto(category1.getName());
        category2NewDto = new NewCategoryDto(category2.getName());
        category1Dto = new CategoryDto(category1.getId(), category1.getName());
        category2Dto = new CategoryDto(category2.getId(), category2.getName());
        service.createCategory(category1NewDto);
        service.createCategory(category2NewDto);
    }

    @Test
    public void shouldReturnCategories() {
        List<CategoryDto> result = service.getCategories(0, 10);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(category1.getId());
        assertThat(result.get(0).getName()).isEqualTo(category1.getName());
        assertThat(result.get(1).getId()).isEqualTo(category2.getId());
        assertThat(result.get(1).getName()).isEqualTo(category2.getName());
    }

    @Test
    public void shouldReturnCategoryById() {
        CategoryDto categoryDto = service.getCategory(2);
        assertThat(categoryDto.getId()).isEqualTo(category2.getId());
        assertThat(categoryDto.getName()).isEqualTo(category2.getName());
    }

    @Test
    public void shouldCreateCategory() {
        NewCategoryDto newCategoryDto = new NewCategoryDto("category_name3");
        service.createCategory(newCategoryDto);
        List<CategoryDto> result = service.getCategories(0, 10);

        assertThat(result.size()).isEqualTo(3);
        CategoryDto categoryDto = service.getCategory(3);
        assertThat(categoryDto.getName()).isEqualTo(newCategoryDto.getName());
    }

    @Test
    public void shouldDeleteCategoryById() {
        service.deleteCategory(2);
        List<CategoryDto> result = service.getCategories(0, 10);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(category1.getId());
        assertThat(result.get(0).getName()).isEqualTo(category1.getName());
    }

    @Test
    public void shouldUpdateCategoryById() {


    }

}
