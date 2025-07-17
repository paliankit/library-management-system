package com.demo.library.management.dto;

import com.demo.library.management.model.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotBlank(message ="Title is required")
    private String title;

    @NotBlank(message ="Author is required")
    private String author;

    @NotBlank(message ="ISBN is required")
    private String isbn;

    private LocalDate publishedDate;

    @NotNull(message = "Status is required")
    private BookStatus status;
}

