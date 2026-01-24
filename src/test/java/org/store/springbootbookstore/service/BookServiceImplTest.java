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
import org.store.springbootbookstore.dto.book.BookDto;
import org.store.springbootbookstore.dto.book.BookDtoWithoutCategories;
import org.store.springbootbookstore.dto.book.CreateBookRequestDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.BookMapper;
import org.store.springbootbookstore.model.Book;
import org.store.springbootbookstore.repository.BookRepository;
import org.store.springbootbookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
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
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("save(): given valid request, returns BookDto and persists entity")
    void save_ValidRequest_ReturnsDto() {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Clean Code");
        request.setAuthor("Robert C. Martin");
        request.setIsbn("9780132350884");
        request.setPrice(new BigDecimal("39.99"));
        request.setDescription("A book about writing cleaner code");
        request.setCoverImage("img.png");
        request.setCategoriesIds(List.of(1L, 2L));

        Book toSave = new Book();
        toSave.setTitle(request.getTitle());
        toSave.setAuthor(request.getAuthor());
        toSave.setIsbn(request.getIsbn());
        toSave.setPrice(request.getPrice());

        Book saved = new Book();
        saved.setId(10L);
        saved.setTitle(request.getTitle());
        saved.setAuthor(request.getAuthor());
        saved.setIsbn(request.getIsbn());
        saved.setPrice(request.getPrice());

        BookDto expectedDto = new BookDto(
                10L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                new BigDecimal("39.99"),
                "A book about writing cleaner code",
                "img.png",
                List.of(1L, 2L)
        );

        when(bookMapper.toModel(request)).thenReturn(toSave);
        when(bookRepository.save(toSave)).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(expectedDto);

        BookDto actual = bookService.save(request);

        assertEquals(expectedDto, actual);

        verify(bookMapper).toModel(request);
        verify(bookRepository).save(toSave);
        verify(bookMapper).toDto(saved);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("findAll(): returns Page<BookDto> mapped from repository Page<Book>")
    void findAll_ValidRequest_ReturnsPageOfDtos() {
        Pageable pageable = PageRequest.of(0, 2);

        Book b1 = new Book();
        b1.setId(1L);
        b1.setTitle("A");

        Book b2 = new Book();
        b2.setId(2L);
        b2.setTitle("B");

        Page<Book> booksPage = new PageImpl<>(List.of(b1, b2), pageable, 2);

        BookDto d1 = new BookDto(1L, "A", "auth1", "isbn1",
                new BigDecimal("10.00"), null, null, List.of());
        BookDto d2 = new BookDto(2L, "B", "auth2", "isbn2",
                new BigDecimal("20.00"), null, null, List.of());

        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(b1)).thenReturn(d1);
        when(bookMapper.toDto(b2)).thenReturn(d2);

        Page<BookDto> result = bookService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(List.of(d1, d2), result.getContent());

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(b1);
        verify(bookMapper).toDto(b2);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("findById(): when book exists, returns mapped BookDto")
    void findById_WhenExists_ShouldReturnDto() {
        Long id = 5L;

        Book book = new Book();
        book.setId(id);
        book.setTitle("Domain-Driven Design");

        BookDto dto = new BookDto(
                id,
                "Domain-Driven Design",
                "Eric Evans",
                "9780321125217",
                new BigDecimal("50.00"),
                null,
                null,
                List.of()
        );

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(dto);

        BookDto result = bookService.findById(id);

        assertEquals(dto, result);

        verify(bookRepository).findById(id);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("findById(): when book missing, throws EntityNotFoundException")
    void findById_WhenMissing_ShouldThrowEntityNotFoundException() {
        Long id = 999L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(id)
        );

        assertTrue(ex.getMessage().contains("Can't find book by id: " + id));

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository, bookMapper);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("deleteById(): calls repository.deleteById")
    void deleteById_ValidRequest_ShouldCallRepository() {
        Long id = 7L;

        bookService.deleteById(id);

        verify(bookRepository).deleteById(id);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("updateById(): when book exists, updates entity, saves, and returns BookDto")
    void updateById_WhenExists_ShouldUpdateEntitySaveAndReturnDto() {
        Long id = 3L;

        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("New title");
        request.setAuthor("New author");
        request.setIsbn("9780132350884");
        request.setPrice(new BigDecimal("99.99"));
        request.setCategoriesIds(List.of(1L));

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("Old title");

        Book saved = new Book();
        saved.setId(id);
        saved.setTitle("New title");

        BookDto expectedDto = new BookDto(
                id,
                "New title",
                "New author",
                "9780132350884",
                new BigDecimal("99.99"),
                null,
                null,
                List.of(1L)
        );

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(bookRepository.save(existing)).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(expectedDto);

        BookDto actual = bookService.updateById(id, request);

        assertEquals(expectedDto, actual);

        verify(bookRepository).findById(id);
        verify(bookMapper).updateEntity(request, existing);
        verify(bookRepository).save(existing);
        verify(bookMapper).toDto(saved);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("updateById(): when book missing, throws EntityNotFoundException")
    void updateById_WhenMissing_ShouldThrowEntityNotFoundException() {
        Long id = 404L;
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("X");
        request.setAuthor("Y");
        request.setIsbn("9780132350884");
        request.setPrice(new BigDecimal("1.00"));
        request.setCategoriesIds(List.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateById(id, request)
        );

        assertTrue(ex.getMessage().contains("Can't find book by id: " + id));

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository, bookMapper);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("findAllByCategoryId(): returns Page<BookDtoWithoutCategories> mapped from repository results")
    void findAllByCategoryId_ValidRequest_ReturnsPageOfDtoWithoutCategories() {
        Long categoryId = 11L;
        Pageable pageable = PageRequest.of(0, 2);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book2");

        Page<Book> page = new PageImpl<>(List.of(book1, book2), pageable, 2);

        BookDtoWithoutCategories dto1 = new BookDtoWithoutCategories(
                1L, "Book1", "A1", "I1", new BigDecimal("10.00"), null, null
        );
        BookDtoWithoutCategories dto2 = new BookDtoWithoutCategories(
                2L, "Book2", "A2", "I2", new BigDecimal("20.00"), null, null
        );

        when(bookRepository.findBooksByCategories_Id(categoryId, pageable)).thenReturn(page);
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);

        Page<BookDtoWithoutCategories> result = bookService.findAllByCategoryId(categoryId, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(List.of(dto1, dto2), result.getContent());

        verify(bookRepository).findBooksByCategories_Id(categoryId, pageable);
        verify(bookMapper).toDtoWithoutCategories(book1);
        verify(bookMapper).toDtoWithoutCategories(book2);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}

