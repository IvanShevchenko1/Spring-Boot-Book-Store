package org.store.springbootbookstore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.CategoryMapper;
import org.store.springbootbookstore.model.Category;
import org.store.springbootbookstore.repository.CategoryRepository;
import org.store.springbootbookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("findAll(): returns Page<CategoryResponseDto> mapped from repository results")
    void findAll_ShouldReturnPageOfDtos() {
        Pageable pageable = PageRequest.of(0, 2);

        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Fiction");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Science");

        Page<Category> page = new PageImpl<>(List.of(c1, c2), pageable, 2);

        CategoryResponseDto d1 = new CategoryResponseDto(1L, "Fiction", "desc1");
        CategoryResponseDto d2 = new CategoryResponseDto(2L, "Science", "desc2");

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(c1)).thenReturn(d1);
        when(categoryMapper.toDto(c2)).thenReturn(d2);

        Page<CategoryResponseDto> result = categoryService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(List.of(d1, d2), result.getContent());

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(c1);
        verify(categoryMapper).toDto(c2);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("save(): given valid request, saves entity and returns CategoryResponseDto")
    void save_WithAValidRequest_SaveEntityAndReturnDto() {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto("Kids", "Books for kids");

        Category toSave = new Category();
        toSave.setName("Kids");
        toSave.setDescription("Books for kids");

        Category saved = new Category();
        saved.setId(10L);
        saved.setName("Kids");
        saved.setDescription("Books for kids");

        CategoryResponseDto expected = new CategoryResponseDto(10L, "Kids", "Books for kids");

        when(categoryMapper.toModel(request)).thenReturn(toSave);
        when(categoryRepository.save(toSave)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.save(request);

        assertEquals(expected, actual);

        verify(categoryMapper).toModel(request);
        verify(categoryRepository).save(toSave);
        verify(categoryMapper).toDto(saved);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("updateById(): when category exists, updates entity, saves, and returns CategoryResponseDto")
    void updateById_WhenExists_ShouldUpdateEntitySaveAndReturnDto() {
        Long id = 5L;
        CreateCategoryRequestDto request = new CreateCategoryRequestDto("Updated", "Updated desc");

        Category existing = new Category();
        existing.setId(id);
        existing.setName("Old");
        existing.setDescription("Old desc");

        Category saved = new Category();
        saved.setId(id);
        saved.setName("Updated");
        saved.setDescription("Updated desc");

        CategoryResponseDto expected = new CategoryResponseDto(id, "Updated", "Updated desc");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.updateById(id, request);

        assertEquals(expected, actual);

        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateEntity(request, existing);
        verify(categoryRepository).save(existing);
        verify(categoryMapper).toDto(saved);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("updateById(): when category is missing, throws EntityNotFoundException")
    void updateById_WhenMissing_ShouldThrowEntityNotFoundException() {
        Long id = 404L;
        CreateCategoryRequestDto request = new CreateCategoryRequestDto("X", "Y");

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.updateById(id, request)
        );

        assertTrue(ex.getMessage().contains("Can't find category by id: " + id));

        verify(categoryRepository).findById(id);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("deleteById(): calls repository.deleteById")
    void deleteById_ShouldCallRepository() {
        Long id = 7L;

        categoryService.deleteById(id);

        verify(categoryRepository).deleteById(id);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }
}
