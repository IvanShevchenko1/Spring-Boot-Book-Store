package org.store.springbootbookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.store.springbootbookstore.config.SecurityConfig;
import org.store.springbootbookstore.dto.book.BookDtoWithoutCategories;
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;
import org.store.springbootbookstore.security.JwtAuthenticationFilter;
import org.store.springbootbookstore.security.JwtUtil;
import org.store.springbootbookstore.service.BookService;
import org.store.springbootbookstore.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = true)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    private CategoryResponseDto sampleCategory(Long id) {
        return new CategoryResponseDto(id, "Programming", "Books about programming");
    }

    private CreateCategoryRequestDto validCreateCategoryRequest() {
        return new CreateCategoryRequestDto("Programming", "Books about programming");
    }

    private CreateCategoryRequestDto invalidCreateCategoryRequestBlankName() {
        return new CreateCategoryRequestDto("   ", "Books about programming");
    }

    private static BookDtoWithoutCategories sampleBookWithoutCategories(Long id) {
        return new BookDtoWithoutCategories(
                id,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                new BigDecimal("39.99"),
                "A Handbook of Agile Software Craftsmanship",
                "https://example.com/cover.png"
        );
    }

    @Test
    @DisplayName("GET /categories without authentication -> 401")
    void getAll_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(categoryService, bookService);
    }

    @Test
    @DisplayName("GET /categories with USER -> 200 OK")
    @WithMockUser(authorities = {"USER"})
    void getAll_withUser_returnsOk() throws Exception {
        Page<CategoryResponseDto> page = new PageImpl<>(
                List.of(sampleCategory(1L), sampleCategory(2L)),
                PageRequest.of(0, 20),
                2
        );
        when(categoryService.findAll(ArgumentMatchers.any())).thenReturn(page);

        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(categoryService, times(1)).findAll(ArgumentMatchers.any());
        verifyNoMoreInteractions(categoryService);
        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("POST /categories with ADMIN -> 201 Created")
    @WithMockUser(authorities = {"ADMIN"})
    void create_withAdmin_returnsCreated() throws Exception {
        when(categoryService.save(ArgumentMatchers.any(CreateCategoryRequestDto.class)))
                .thenReturn(sampleCategory(1L));

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateCategoryRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Programming"));

        verify(categoryService, times(1)).save(ArgumentMatchers.any(CreateCategoryRequestDto.class));
        verifyNoMoreInteractions(categoryService);
        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("POST /categories with ADMIN and invalid body -> 400 Bad Request")
    @WithMockUser(authorities = {"ADMIN"})
    void create_withAdmin_invalidBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateCategoryRequestBlankName())))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService, bookService);
    }

    @Test
    @DisplayName("PUT /categories/{id} with ADMIN -> 200 OK")
    @WithMockUser(authorities = {"ADMIN"})
    void update_withAdmin_returnsOk() throws Exception {
        when(categoryService.updateById(eq(5L), ArgumentMatchers.any(CreateCategoryRequestDto.class)))
                .thenReturn(sampleCategory(5L));

        mockMvc.perform(put("/categories/{id}", 5L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateCategoryRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.name").value("Programming"));

        verify(categoryService, times(1))
                .updateById(eq(5L), ArgumentMatchers.any(CreateCategoryRequestDto.class));
        verifyNoMoreInteractions(categoryService);
        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("DELETE /categories/{id} with ADMIN -> 204 No Content")
    @WithMockUser(authorities = {"ADMIN"})
    void delete_withAdmin_returnsNoContent() throws Exception {
        doNothing().when(categoryService).deleteById(7L);

        mockMvc.perform(delete("/categories/{id}", 7L).with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(7L);
        verifyNoMoreInteractions(categoryService);
        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("GET /categories/{id}/books with USER -> 200 OK")
    @WithMockUser(authorities = {"USER"})
    void getAllBooksByCategory_withUser_returnsOk() throws Exception {
        Page<BookDtoWithoutCategories> page = new PageImpl<>(
                List.of(sampleBookWithoutCategories(1L), sampleBookWithoutCategories(2L)),
                PageRequest.of(0, 20),
                2
        );
        when(bookService.findAllByCategoryId(eq(3L), ArgumentMatchers.any())).thenReturn(page);

        mockMvc.perform(get("/categories/{id}/books", 3L)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(bookService, times(1)).findAllByCategoryId(eq(3L), ArgumentMatchers.any());
        verifyNoMoreInteractions(bookService);
        verifyNoInteractions(categoryService);
    }

    @Test
    @DisplayName("GET /categories/{id}/books without authentication -> 401")
    void getAllBooksByCategory_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/categories/{id}/books", 3L))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(categoryService, bookService);
    }
}
