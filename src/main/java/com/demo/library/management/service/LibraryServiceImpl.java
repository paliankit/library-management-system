package com.demo.library.management.service;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.exception.BookNotFoundException;
import com.demo.library.management.mapper.BookMapper;
import com.demo.library.management.model.Book;
import com.demo.library.management.model.BookStatus;
import com.demo.library.management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<BookResponseDTO> getAllBooksFiltered(String author, String status) {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .filter(book -> author == null || book.getAuthor().equalsIgnoreCase(author))
                .filter(book -> {
                    if (status == null) return true;
                    try {
                        return book.getStatus() == BookStatus.valueOf(status.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return bookMapper.toDTO(book);
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO updatedBookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(updatedBookDTO.getTitle());
        existingBook.setAuthor(updatedBookDTO.getAuthor());
        existingBook.setIsbn(updatedBookDTO.getIsbn());
        existingBook.setPublishedDate(updatedBookDTO.getPublishedDate());
        existingBook.setStatus(updatedBookDTO.getStatus());

        Book updated = bookRepository.save(existingBook);
        return bookMapper.toDTO(updated);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<BookResponseDTO> getBooksPublishedAfter(LocalDate date) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getPublishedDate() != null && book.getPublishedDate().isAfter(date))
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }
}
