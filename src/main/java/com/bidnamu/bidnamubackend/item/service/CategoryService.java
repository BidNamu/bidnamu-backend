package com.bidnamu.bidnamubackend.item.service;

import com.bidnamu.bidnamubackend.item.domain.Category;
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
        final Category category = categoryRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException(CATEGORY_NOT_FOUND_MESSAGE.formatted(id)));
        return CategoryResultDto.from(category);
    }
}
