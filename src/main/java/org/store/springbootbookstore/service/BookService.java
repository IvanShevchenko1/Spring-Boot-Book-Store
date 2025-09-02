package org.store.springbootbookstore.service;

import java.util.List;
import org.store.springbootbookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
