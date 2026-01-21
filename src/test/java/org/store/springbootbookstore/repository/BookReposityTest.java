package org.store.springbootbookstore.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.store.springbootbookstore.model.Book;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class BookReposityTest { 

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("findBooksByCategories_Id: when categoryId = 1, returns exactly 2 books")
    @Sql(
            scripts = "classpath:database/books/add_test_book_with_category_id_1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(value = {
            "classpath:database/books/delete_testl_books.sql",
            "classpath:database/books/delete_test_books_categories.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findBooksByCategoriesId_CategoryIdIsOne_ReturnsTwoBooks() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> books = bookRepository.findBooksByCategories_Id(1L,pageable);
        Assertions.assertEquals(2, books.getContent().size());
    }
}
