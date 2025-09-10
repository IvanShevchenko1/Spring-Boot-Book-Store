package org.store.springbootbookstore.mapper;

import org.mapstruct.Mapper;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.BookDto;
import org.store.springbootbookstore.dto.CreateBookRequestDto;
import org.store.springbootbookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
