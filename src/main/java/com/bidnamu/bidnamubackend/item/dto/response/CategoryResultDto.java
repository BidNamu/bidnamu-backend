package com.bidnamu.bidnamubackend.item.dto.response;

import com.bidnamu.bidnamubackend.item.domain.Category;
import java.util.List;
import lombok.Getter;

@Getter
public record CategoryResultDto(
    Long id,
    String name,
    Long depth,
    List<CategoryResultDto> children
) {

    public static CategoryResultDto from(Category category) {
        return new CategoryResultDto(
            category.getId(),
            category.getName(),
            category.getDepth(),
            category.getChildren().stream().map(CategoryResultDto::from).toList()
        );
    }
}
