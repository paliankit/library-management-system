package com.demo.library.management.dto;

import com.demo.library.management.model.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


public class BookRequestDTO {

    @NotBlank(message ="Title is required")
    private String title;

    @NotBlank(message ="Author is required")
    private String author;

    @NotBlank(message ="ISBN is required")
    private String isbn;

    private LocalDate publishedDate;

    public BookRequestDTO() {
    }

    public BookRequestDTO(String title, String author, String isbn, LocalDate publishedDate, BookStatus status) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.status = status;
    }

    @NotNull(message = "Status is required")
    private BookStatus status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}

