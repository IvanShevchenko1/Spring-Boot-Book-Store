package org.store.springbootbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.store.springbootbookstore.dto.book.BookDtoWithoutCategories;
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;
import org.store.springbootbookstore.service.BookService;
import org.store.springbootbookstore.service.CategoryService;

@Tag(name = "Categories", description = "Endpoint for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    private final BookService bookService;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all Categories",
            description = "Retrieve a paginated list of all available categories")
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Category",
            description = "Add a new category to the store")
    public CategoryResponseDto createCategory(
            @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update category",
            description = "Update an existing category with new details by its ID")
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                               @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.updateById(id, requestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category",
            description = "Remove a category from the store by its ID")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all books in a certain category",
            description = "Retrieve a paginated list of all books in a certain category by its ID")
    public Page<BookDtoWithoutCategories> getAllBooksByCategory(
            @PathVariable Long id, Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
