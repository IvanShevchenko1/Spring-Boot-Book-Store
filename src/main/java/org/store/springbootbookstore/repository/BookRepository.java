package org.store.springbootbookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findBooksByCategories_Id(Long categoriesId, Pageable pageable);
}
