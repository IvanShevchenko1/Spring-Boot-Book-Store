package org.store.springbootbookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.BookDto;
import org.store.springbootbookstore.dto.CreateBookRequestDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.BookMapper;
import org.store.springbootbookstore.model.Book;
import org.store.springbootbookstore.repository.BookRepository;
import org.store.springbootbookstore.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository
                .save(bookMapper
                        .toModel(requestDto)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book bookById = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        );
        return bookMapper.toDto(bookById);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
        bookMapper.updateEntity(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }
}
