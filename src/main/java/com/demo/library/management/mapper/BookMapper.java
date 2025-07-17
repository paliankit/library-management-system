package com.demo.library.management.mapper;


import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequestDTO dto) {
        return new Book(null, dto.getTitle(), dto.getAuthor(), dto.getIsbn(), dto.getPublishedDate(), dto.getStatus());
    }

    public BookResponseDTO toDTO(Book book) {
        return new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate(), book.getStatus());
    }
}
