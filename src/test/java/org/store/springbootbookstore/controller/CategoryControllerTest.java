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
import org.store.springbootbookstore.dto.category.CategoryResponseDto;
import org.store.springbootbookstore.dto.category.CreateCategoryRequestDto;
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
class CategoryControllerTest {

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
                    new ClassPathResource("database/categories/delete_test_category.sql"));
        }
    }

    @Test
    @DisplayName("GET /categories without authentication -> 401")
    void getAll_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /categories with ADMIN -> 201 Created")
    @WithMockUser(authorities = {"ADMIN"})
    void create_WithAdmin_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto expected = TestUtil.validCreateCategoryRequest();

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expected))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("POST /categories with ADMIN and invalid body -> 400 Bad Request")
    @WithMockUser(authorities = {"ADMIN"})
    void create_WithAdminInvalidBody_ReturnsBadRequest() throws Exception {
        CreateCategoryRequestDto badRequest = TestUtil.invalidCreateCategoryRequest();

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(badRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /categories with USER -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    void create_WithUser_Forbidden() throws Exception {
        CreateCategoryRequestDto request = TestUtil.validCreateCategoryRequest();

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /categories/{id} with ADMIN -> 200 OK")
    @WithMockUser(authorities = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/categories/add_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void update_WithAdmin_ReturnsOk() throws Exception {
        CreateCategoryRequestDto request = TestUtil.validCreateCategoryRequest();
        CategoryResponseDto expected = TestUtil.sampleCategoryDto(1L);
        MvcResult result = mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("PUT /categories/{id} with USER -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    @Sql(
            scripts = "classpath:database/categories/add_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void update_WithUser_Forbidden() throws Exception {
        CreateCategoryRequestDto request = TestUtil.validCreateCategoryRequest();

        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /categories/{id} with ADMIN -> 204 No Content")
    @WithMockUser(authorities = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/categories/add_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void delete_WithAdmin_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 2L).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /categories/{id} with USER -> 403 Forbidden")
    @WithMockUser(authorities = {"USER"})
    @Sql(
            scripts = "classpath:database/categories/add_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void delete_WithUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /categories/{id}/books without authentication -> 401")
    void getAllBooksByCategory_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/categories/{id}/books", 1L))
                .andExpect(status().isUnauthorized());
    }
}
