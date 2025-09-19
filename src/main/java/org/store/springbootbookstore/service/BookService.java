package org.store.springbootbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.store.springbootbookstore.dto.BookDto;
import org.store.springbootbookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);
}
