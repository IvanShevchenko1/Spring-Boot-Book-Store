package org.store.springbootbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto save(CreateCategoryRequestDto requestDto);

    CategoryResponseDto updateById(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
