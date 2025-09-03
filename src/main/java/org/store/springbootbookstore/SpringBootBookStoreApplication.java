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
            Book bookCleanCode = new Book();
            bookCleanCode.setTitle("Clean Code");
            bookCleanCode.setAuthor("Robert C. Martin");
            bookCleanCode.setIsbn("9780132350884");
            bookCleanCode.setPrice(BigDecimal.valueOf(45.50));
            bookCleanCode.setDescription("A Handbook of Agile Software Craftsmanship");
            bookCleanCode.setCoverImage("1111.jpg");
            bookService.save(bookCleanCode);

            Book bookJava = new Book();
            bookJava.setTitle("Effective Java");
            bookJava.setAuthor("Joshua Bloch");
            bookJava.setIsbn("9780134685991");
            bookJava.setPrice(BigDecimal.valueOf(55.99));
            bookJava.setDescription("Best practices for the Java platform");
            bookJava.setCoverImage("2222.jpg");
            bookService.save(bookJava);

            Book bookPatterns = new Book();
            bookPatterns.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
            bookPatterns.setAuthor("Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
            bookPatterns.setIsbn("9780201633610");
            bookPatterns.setPrice(BigDecimal.valueOf(60.00));
            bookPatterns.setDescription("Classic book on design patterns in software engineering");
            bookPatterns.setCoverImage("3333.jpg");
            bookService.save(bookPatterns);

            Book bookSpring = new Book();
            bookSpring.setTitle("Spring in Action");
            bookSpring.setAuthor("Craig Walls");
            bookSpring.setIsbn("9781617294945");
            bookSpring.setPrice(BigDecimal.valueOf(50.00));
            bookSpring.setDescription("Comprehensive guide to Spring Framework and Spring Boot");
            bookSpring.setCoverImage("4444.jpg");
            bookService.save(bookSpring);

            System.out.println("All books in database:");
            bookService.findAll().forEach(System.out::println);
        };
    }
}
