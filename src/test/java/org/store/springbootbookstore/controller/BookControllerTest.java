package org.store.springbootbookstore.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.store.springbootbookstore.dto.book.BookDto;
import org.store.springbootbookstore.dto.book.CreateBookRequestDto;
import org.store.springbootbookstore.util.TestUtil;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import javax.sql.DataSource;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = true)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    @AfterEach
    void cleanup() {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(true);
            ScriptUtils.executeSqlScript(con,
                    new ClassPathResource("database/books/delete_test_books_categories.sql"));
            ScriptUtils.executeSqlScript(con,
                    new ClassPathResource("database/books/delete_test_books.sql"));
        }
    }

    @Test
    @DisplayName("GET /books without authentication -> 401 Unauthorized")
    void getAll_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /books with USER authority -> 200 OK")
    @WithMockUser(authorities = {"USER"})
    @Sql(
            scripts = "classpath:database/books/add_test_book_with_category_id_1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void getAll_WithUser_ReturnsOkAndContainsInsertedBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = root.get("content");

        assertTrue(contentNode.isArray());
        assertFalse(contentNode.isEmpty());

        boolean found = false;
        for (JsonNode b : contentNode) {
            if (b.get("id").asLong() == 2L) {
                found = true;
                assertEquals("Book2", b.get("title").asText());
                assertEquals("JohnDoe", b.get("author").asText());
                assertEquals("654321", b.get("isbn").asText());
                break;
            }
        }
        assertTrue(found, "Expected book was not found in page content");
    }

    @Test
    @DisplayName("GET /books/{id} with USER authority for missing id -> 404")
    @WithMockUser(authorities = {"USER"})
    void getById_Missing_Returns404() throws Exception {
        mockMvc.perform(get("/books/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /books with ADMIN authority -> 201 Created")
    @WithMockUser(authorities = {"ADMIN"})
    void create_WithAdmin_ReturnsCreated() throws Exception {
        CreateBookRequestDto request = TestUtil.validCreateRequest();
        BookDto expected = TestUtil.sampleBookDto(2L);
        MvcResult result = mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        EqualsBuilder.reflectionEquals(expected, actualDto, "id");
    }

    @Test
    @DisplayName("POST /books with USER authority -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    void create_WithUser_Forbidden() throws Exception {
        CreateBookRequestDto request = TestUtil.validCreateRequest();

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /books with ADMIN authority and invalid body -> 400 Bad Request")
    @WithMockUser(authorities = {"ADMIN"})
    void create_WithAdminAndInvalidBody_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto badRequest = TestUtil.invalidCreateRequestMissingTitle();

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(badRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /books/{id} with ADMIN authority -> 200 OK")
    @WithMockUser(authorities = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/books/add_test_book_with_category_id_1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void update_WithAdmin_ReturnsOk() throws Exception {
        CreateBookRequestDto request = TestUtil.validCreateRequest();
        BookDto expected = TestUtil.sampleBookDto(2L);

        MvcResult result = mockMvc.perform(put("/books/{id}", 2L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        BookDto updatedDto = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        EqualsBuilder.reflectionEquals(expected, updatedDto, "id");
    }

    @Test
    @DisplayName("PUT /books/{id} with USER authority -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    @Sql(
            scripts = "classpath:database/books/add_test_book_with_category_id_1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void update_WithUser_Forbidden() throws Exception {
        CreateBookRequestDto request = TestUtil.validCreateRequest();

        mockMvc.perform(put("/books/{id}", 2L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /books/{id} with ADMIN authority -> 204 No Content")
    @WithMockUser(authorities = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/books/add_test_book_with_category_id_1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void delete_WithAdmin_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", 2L).with(csrf()))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("DELETE /books/{id} with USER authority -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    void delete_WithUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/books/{id}", 2L))
                .andExpect(status().isForbidden());
    }
}
