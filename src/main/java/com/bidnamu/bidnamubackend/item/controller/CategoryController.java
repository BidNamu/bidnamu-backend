package com.bidnamu.bidnamubackend.item.controller;

import com.bidnamu.bidnamubackend.item.dto.request.CategoryFormDto;
import com.bidnamu.bidnamubackend.item.dto.response.CategoryResultDto;
import com.bidnamu.bidnamubackend.item.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResultDto>> getAllCategories() {
        final List<CategoryResultDto> categories = categoryService.getCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResultDto> getCategory(@PathVariable final Long id) {
        final CategoryResultDto category = categoryService.getCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResultDto> createCategory(
        @RequestBody @Valid final CategoryFormDto form) {
        final CategoryResultDto category = categoryService.createCategory(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResultDto> updateCategory(
        @RequestBody @Valid final CategoryFormDto form) {
        final CategoryResultDto category = categoryService.updateCategory(form);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }
}
