package org.store.springbootbookstore.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;
import org.store.springbootbookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CreateCategoryRequestDto dto, @MappingTarget Category entity);

    @Named("idsToCategories")
    default Set<Category> idsToCategories(List<Long> ids) {
        return ids.stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }

    @Named("categoriesToIds")
    default List<Long> categoriesToIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .toList();
    }
}
