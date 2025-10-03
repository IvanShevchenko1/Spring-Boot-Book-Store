package org.store.springbootbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.store.springbootbookstore.dto.book.BookDto;
import org.store.springbootbookstore.dto.book.BookDtoWithoutCategories;
import org.store.springbootbookstore.dto.book.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    Page<BookDtoWithoutCategories> findAllByCategoryId(Long id, Pageable pageable);
}
