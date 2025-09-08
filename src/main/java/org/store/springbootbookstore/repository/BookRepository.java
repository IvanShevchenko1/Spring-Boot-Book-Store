package org.store.springbootbookstore.repository;

import java.util.List;
import java.util.Optional;
import org.store.springbootbookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
