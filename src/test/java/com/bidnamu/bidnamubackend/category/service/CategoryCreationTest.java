package com.bidnamu.bidnamubackend.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.request.CategoryFormDto;
import com.bidnamu.bidnamubackend.auction.dto.response.CategoryResultDto;
import com.bidnamu.bidnamubackend.auction.repository.CategoryRepository;
import com.bidnamu.bidnamubackend.auction.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryCreationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("최상위 Category 저장 요청하였을 경우 DB에 저장된다")
    void topCategorySaveTest() {
        // Given
        final String categoryName = "카테고리";
        final CategoryFormDto form = CategoryFormDto.builder().name(categoryName).build();

        // When
        final CategoryResultDto result = categoryService.createCategory(form);

        // Then
        assertEquals(result.name(), categoryName);
        assertEquals(0, result.depth());
    }

    @Test
    @DisplayName("하위 Category 저장을 요청하였을 경우 DB에 저장된다")
    void childrenCategorySaveTest() {
        // Given
        final var dep1 =
            categoryRepository.save(Category.builder().name("dep1").build());

        final var form = CategoryFormDto.builder().name("dep2").parent(dep1.getId()).build();

        // When
        final var dep2Dto = categoryService.createCategory(form);
        final var dep2Entity = categoryRepository.findById(dep2Dto.id()).orElseThrow();

        // Then
        assertEquals(dep2Entity.getParent(), dep1);
    }
}
