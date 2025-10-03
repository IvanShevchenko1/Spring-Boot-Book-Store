package org.store.springbootbookstore.dto.category;

public record CategoryResponseDto(
        Long id,
        String name,
        String description
) {
}
