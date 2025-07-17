package com.demo.library.management.controller;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.model.BookStatus;
import com.demo.library.management.service.LibraryService;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    private ObjectMapper objectMapper;

    private BookResponseDTO mockBookResponse;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockBookResponse = new BookResponseDTO(
                1L, "Test Title", "Test Author", "1234567890",
                LocalDate.of(2020, 1, 1), com.demo.library.management.model.BookStatus.AVAILABLE
        );
    }

    @Test
    void testGetBookByIdSuccess() throws Exception {
        when(libraryService.getBookById(1L)).thenReturn(mockBookResponse);

        mockMvc.perform(get("/v1/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteBookSuccess() throws Exception {
        doNothing().when(libraryService).deleteBook(1L);

        mockMvc.perform(delete("/v1/library/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllBooksWithFilters() throws Exception {
        when(libraryService.getAllBooksFiltered("Test Author", "AVAILABLE")).thenReturn(List.of(mockBookResponse));

        mockMvc.perform(get("/v1/library/books?author=Test Author&status=AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Test Author"));
    }

    @Test
    void testGetBooksPublishedAfter() throws Exception {
        when(libraryService.getBooksPublishedAfter(LocalDate.of(2019, 1, 1))).thenReturn(List.of(mockBookResponse));

        mockMvc.perform(get("/v1/library/books/publishedAfter?date=2019-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void testCreateBookValidationError() throws Exception {
        BookRequestDTO invalidRequest = new BookRequestDTO("", "", "123", null, null);

        mockMvc.perform(post("/v1/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
