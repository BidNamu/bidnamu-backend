package com.bidnamu.bidnamubackend.item.service;

import com.bidnamu.bidnamubackend.item.domain.Category;
import com.bidnamu.bidnamubackend.item.dto.request.CategoryFormDto;
import com.bidnamu.bidnamubackend.item.dto.response.CategoryResultDto;
import com.bidnamu.bidnamubackend.item.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "해당 Category를 찾을 수 없습니다 : %d";

    @Transactional(readOnly = true)
    public List<CategoryResultDto> getCategories() {
        return categoryRepository.findAllByParentIsNull()
            .stream()
            .map(CategoryResultDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResultDto getCategory(final Long id) {
        final Category category = findCategoryById(id);
        return CategoryResultDto.from(category);
    }

    @Transactional
    public CategoryResultDto createCategory(final CategoryFormDto categoryFormDto) {
        if (categoryFormDto.name().isBlank()) {
            throw new IllegalArgumentException("Category 의 name 이 비어있습니다.");
        }
        return CategoryResultDto.from(categoryRepository.save(toEntity(categoryFormDto)));
    }

    @Transactional
    public CategoryResultDto updateCategory(final CategoryFormDto categoryFormDto) {
        if (categoryFormDto.id() == null) {
            throw new IllegalArgumentException("Category 의 ID가 null 입니다.");
        }
        final Category category = findCategoryById(categoryFormDto.id());

        if (categoryFormDto.parent() != null) {
            final Category parent = findCategoryById(categoryFormDto.parent());
            category.updateParent(parent);
        }
        if (!categoryFormDto.name().isBlank()) {
            category.updateName(categoryFormDto.name());
        }

        return CategoryResultDto.from(category);
    }

    private Category toEntity(final CategoryFormDto categoryFormDto) {
        final var parent =
            categoryFormDto.parent() != null && categoryFormDto.parent() != 0
                ? findCategoryById(categoryFormDto.parent()) : null;
        return Category.builder()
            .name(categoryFormDto.name())
            .parent(parent)
            .build();
    }

    private Category findCategoryById(final Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
            new NoSuchElementException(CATEGORY_NOT_FOUND_MESSAGE.formatted(id)));
    }
}
