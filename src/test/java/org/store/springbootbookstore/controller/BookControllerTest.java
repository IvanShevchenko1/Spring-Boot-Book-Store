package org.store.springbootbookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.store.springbootbookstore.dto.book.BookDto;
import org.store.springbootbookstore.dto.book.CreateBookRequestDto;
import org.store.springbootbookstore.security.JwtUtil;
import org.store.springbootbookstore.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = true)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @TestConfiguration
    @EnableMethodSecurity(prePostEnabled = true)
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .httpBasic(basic -> {})
                    .build();
        }
    }

    private BookDto sampleBookDto(Long id) {
        return new BookDto(
                id,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                new BigDecimal("39.99"),
                "A Handbook of Agile Software Craftsmanship",
                "https://example.com/cover.png",
                List.of(1L, 2L)
        );
    }

    private CreateBookRequestDto validCreateRequest() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert C. Martin");
        dto.setIsbn("9780132350884");
        dto.setPrice(new BigDecimal("39.99"));
        dto.setDescription("A Handbook of Agile Software Craftsmanship");
        dto.setCoverImage("https://example.com/cover.png");
        dto.setCategoriesIds(List.of(1L, 2L));
        return dto;
    }

    private CreateBookRequestDto invalidCreateRequestMissingTitle() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(" ");
        dto.setAuthor("Robert C. Martin");
        dto.setIsbn("9780132350884");
        dto.setPrice(new BigDecimal("39.99"));
        dto.setCategoriesIds(List.of(1L));
        return dto;
    }

    @Test
    @DisplayName("GET /books without authentication -> 401 Unauthorized (security filter active)")
    void getAll_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("GET /books with USER authority -> 200 OK")
    @WithMockUser(authorities = {"USER"})
    void getAll_withUser_returnsOk() throws Exception {
        Page<BookDto> page = new PageImpl<>(
                List.of(sampleBookDto(1L), sampleBookDto(2L)),
                PageRequest.of(0, 20),
                2
        );
        when(bookService.findAll(ArgumentMatchers.any())).thenReturn(page);

        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(bookService, times(1)).findAll(ArgumentMatchers.any());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @DisplayName("GET /books/{id} with USER authority -> 200 OK")
    @WithMockUser(authorities = {"USER"})
    void getById_withUser_returnsOk() throws Exception {
        when(bookService.findById(10L)).thenReturn(sampleBookDto(10L));

        mockMvc.perform(get("/books/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.title").value("Clean Code"));

        verify(bookService, times(1)).findById(10L);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @DisplayName("POST /books with ADMIN authority -> 201 Created")
    @WithMockUser(authorities = {"ADMIN"})
    void create_withAdmin_returnsCreated() throws Exception {
        CreateBookRequestDto req = validCreateRequest();
        when(bookService.save(ArgumentMatchers.any(CreateBookRequestDto.class)))
                .thenReturn(sampleBookDto(1L));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.isbn").value("9780132350884"));

        verify(bookService, times(1)).save(ArgumentMatchers.any(CreateBookRequestDto.class));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @DisplayName("POST /books with USER authority -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    void create_withUser_forbidden() throws Exception {
        CreateBookRequestDto req = validCreateRequest();

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("POST /books with ADMIN authority and invalid body -> 400 Bad Request")
    @WithMockUser(authorities = {"ADMIN"})
    void create_withAdmin_invalidBody_returnsBadRequest() throws Exception {
        CreateBookRequestDto req = invalidCreateRequestMissingTitle();

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)).with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("PUT /books/{id} with ADMIN authority -> 200 OK")
    @WithMockUser(authorities = {"ADMIN"})
    void update_withAdmin_returnsOk() throws Exception {
        CreateBookRequestDto req = validCreateRequest();
        when(bookService.updateById(eq(5L), ArgumentMatchers.any(CreateBookRequestDto.class)))
                .thenReturn(sampleBookDto(5L));

        mockMvc.perform(put("/books/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        verify(bookService, times(1)).updateById(eq(5L), ArgumentMatchers.any(CreateBookRequestDto.class));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @DisplayName("PUT /books/{id} with USER authority -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    void update_withUser_forbidden() throws Exception {
        CreateBookRequestDto req = validCreateRequest();

        mockMvc.perform(put("/books/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("DELETE /books/{id} with ADMIN authority -> 204 No Content")
    @WithMockUser(authorities = {"ADMIN"})
    void delete_withAdmin_returnsNoContent() throws Exception {
        doNothing().when(bookService).deleteById(7L);

        mockMvc.perform(delete("/books/{id}", 7L).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(bookService, times(1)).deleteById(7L);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @DisplayName("DELETE /books/{id} with USER authority -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    void delete_withUser_forbidden() throws Exception {
        mockMvc.perform(delete("/books/{id}", 7L))
                .andExpect(status().isForbidden());

        verifyNoInteractions(bookService);
    }
}
