package org.store.springbootbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
