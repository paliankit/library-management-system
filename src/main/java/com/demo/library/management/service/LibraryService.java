package com.demo.library.management.service;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface LibraryService {

    public BookResponseDTO createBook(BookRequestDTO dto);
    public List<BookResponseDTO> getAllBooksFiltered(String author, String status);
    public BookResponseDTO getBookById(Long id);
    public BookResponseDTO updateBook(Long id, BookRequestDTO updatedBook);
    public void deleteBook(Long id);
    public List<BookResponseDTO> getBooksPublishedAfter(LocalDate date);
}
