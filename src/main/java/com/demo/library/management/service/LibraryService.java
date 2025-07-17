package com.demo.library.management.service;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;

public interface LibraryService {

    public BookResponseDTO createBook(BookRequestDTO dto);
}
