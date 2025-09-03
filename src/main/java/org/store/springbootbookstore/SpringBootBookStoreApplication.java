package org.store.springbootbookstore;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.store.springbootbookstore.model.Book;
import org.store.springbootbookstore.service.BookService;

@SpringBootApplication
public class SpringBootBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setTitle("Clean Code");
            book1.setAuthor("Robert C. Martin");
            book1.setIsbn("9780132350884");
            book1.setPrice(BigDecimal.valueOf(45.50));
            book1.setDescription("A Handbook of Agile Software Craftsmanship");
            book1.setCoverImage("1111.jpg");
            bookService.save(book1);

            Book book2 = new Book();
            book2.setTitle("Effective Java");
            book2.setAuthor("Joshua Bloch");
            book2.setIsbn("9780134685991");
            book2.setPrice(BigDecimal.valueOf(55.99));
            book2.setDescription("Best practices for the Java platform");
            book2.setCoverImage("2222.jpg");
            bookService.save(book2);

            Book book3 = new Book();
            book3.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
            book3.setAuthor("Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
            book3.setIsbn("9780201633610");
            book3.setPrice(BigDecimal.valueOf(60.00));
            book3.setDescription("Classic book on design patterns in software engineering");
            book3.setCoverImage("3333.jpg");
            bookService.save(book3);

            Book book4 = new Book();
            book4.setTitle("Spring in Action");
            book4.setAuthor("Craig Walls");
            book4.setIsbn("9781617294945");
            book4.setPrice(BigDecimal.valueOf(50.00));
            book4.setDescription("Comprehensive guide to Spring Framework and Spring Boot");
            book4.setCoverImage("4444.jpg");
            bookService.save(book4);

            System.out.println("All books in database:");
            bookService.findAll().forEach(System.out::println);
        };
    }
}
