package org.store.springbootbookstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.book.BookDto;
import org.store.springbootbookstore.dto.book.BookDtoWithoutCategories;
import org.store.springbootbookstore.dto.book.CreateBookRequestDto;
import org.store.springbootbookstore.model.Book;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface BookMapper {
    @Mapping(target = "categoriesIds",
            source = "categories",
            qualifiedByName = "categoriesToIds")
    BookDto toDto(Book book);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "categories",
                    source = "categoriesIds",
                    qualifiedByName = "idsToCategories")
    })
    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategories toDtoWithoutCategories(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categories",
            source = "categoriesIds",
            qualifiedByName = "idsToCategories")
    void updateEntity(CreateBookRequestDto dto, @MappingTarget Book entity);
}
