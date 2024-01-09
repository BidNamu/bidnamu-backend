package com.bidnamu.bidnamubackend.auction.dto.response;

import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

public record CategoryResultDto(
    Long id,
    String name,
    int depth,
    @JsonInclude(Include.NON_EMPTY)
    List<CategoryResultDto> children
) {

    public static CategoryResultDto from(final Category category) {
        return new CategoryResultDto(
            category.getId(),
            category.getName(),
            category.getDepth(),
            category.getChildren().stream().map(CategoryResultDto::from).toList()
        );
    }
}
