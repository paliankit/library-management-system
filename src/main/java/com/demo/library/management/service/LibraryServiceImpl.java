package com.demo.library.management.service;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.mapper.BookMapper;
import com.demo.library.management.model.Book;
import com.demo.library.management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    public BookResponseDTO createBook(BookRequestDTO dto){
        Book entity = bookMapper.toEntity(dto);
        Book saved = bookRepository.save(entity);
        return bookMapper.toDTO(saved);
    }
}
