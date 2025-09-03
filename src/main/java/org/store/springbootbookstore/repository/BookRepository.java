package org.store.springbootbookstore.repository;

import java.util.List;
import org.store.springbootbookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
