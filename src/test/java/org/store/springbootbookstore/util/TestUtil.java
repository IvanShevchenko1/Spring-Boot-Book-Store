package org.store.springbootbookstore.util;

import org.store.springbootbookstore.dto.book.BookDto;
import org.store.springbootbookstore.dto.book.CreateBookRequestDto;
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;

import java.math.BigDecimal;
import java.util.List;

public class TestUtil {

    public static BookDto sampleBookDto(Long id) {
        return new BookDto(
                id,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                new BigDecimal("39.99"),
                "A Handbook of Agile Software Craftsmanship",
                "https://example.com/cover.png",
                List.of(1L)
        );
    }

    public static CreateBookRequestDto validCreateRequest() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert C. Martin");
        dto.setIsbn("9780132350884");
        dto.setPrice(new BigDecimal("39.99"));
        dto.setDescription("A Handbook of Agile Software Craftsmanship");
        dto.setCoverImage("https://example.com/cover.png");
        dto.setCategoriesIds(List.of(1L));
        return dto;
    }

    public static CreateBookRequestDto invalidCreateRequestMissingTitle() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(" ");
        dto.setAuthor("Robert C. Martin");
        dto.setIsbn("9780132350884");
        dto.setPrice(new BigDecimal("39.99"));
        dto.setCategoriesIds(List.of(1L));
        return dto;
    }

    public static CreateCategoryRequestDto validCreateCategoryRequest() {
        return new CreateCategoryRequestDto(
                "Programming",
                "Books about programming"
        );
    }

    public static CreateCategoryRequestDto invalidCreateCategoryRequest() {
        return new CreateCategoryRequestDto(
                "",
                "Books about programming"
        );
    }

    public static CategoryResponseDto sampleCategoryDto(Long id) {
        return new CategoryResponseDto(
                id,
                "Programming",
                "Books about programming"
        );
    }
}
