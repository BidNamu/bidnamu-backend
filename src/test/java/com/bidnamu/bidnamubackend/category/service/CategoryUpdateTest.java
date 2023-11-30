package com.bidnamu.bidnamubackend.category.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.request.CategoryFormDto;
import com.bidnamu.bidnamubackend.auction.repository.CategoryRepository;
import com.bidnamu.bidnamubackend.auction.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryUpdateTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 업데이트 요청 시 DB에 반영되어야 한다")
    void categoryUpdatingTest() {
        // Given
        final var parent1 = categoryRepository.save(Category.builder().name("상위1").build());
        final var parent2 = categoryRepository.save(Category.builder().name("상위2").build());
        final var category = categoryRepository.save(
            Category.builder().name("카테고리").parent(parent1).build());

        // When
        final String updatedName = "변경된 카테고리명";
        final var updatedCategoryDto = categoryService.updateCategory(category.getId(),
            CategoryFormDto.builder().name(updatedName).parent(parent2.getId())
                .build());

        final var updatedEntity = categoryRepository.findById(updatedCategoryDto.id())
            .orElseThrow();

        // Then
        assertEquals(updatedCategoryDto.name(), updatedName);
        assertEquals(parent2, updatedEntity.getParent());
    }

    @Test
    @DisplayName("카테고리 업데이트 요청 시 순환참조가 발생하면 예외를 발생시켜야 한다")
    void circularReferenceTest() {
        // Given
        final var parent = categoryRepository.save(Category.builder().name("상위").build());
        final var children =
            categoryRepository.save(Category.builder().name("하위").parent(parent).build());

        // When
        final var form = CategoryFormDto.builder().parent(children.getId()).build();
        final Long id = parent.getId();

        // Then
        assertThrows(IllegalArgumentException.class,
            () -> categoryService.updateCategory(id, form));
    }
}
