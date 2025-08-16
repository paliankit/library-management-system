package com.demo.library.management.service;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.exception.BookNotFoundException;
import com.demo.library.management.mapper.BookMapper;
import com.demo.library.management.model.Book;
import com.demo.library.management.model.BookStatus;
import com.demo.library.management.repository.BookRepository;
import com.demo.library.management.utility.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String BOOK_KEY = "book:";
    private static final String BOOK_LIST_KEY = "book:list";
    Duration ttl = Duration.ofMinutes(30);

    public BookResponseDTO createBook(BookRequestDTO dto){
        Book entity = bookMapper.toEntity(dto);
        Book saved = bookRepository.save(entity);
        return bookMapper.toDTO(saved);
    }

    public List<BookResponseDTO> getAllBooksFiltered(String author, String status) {
        String redisKey=BOOK_LIST_KEY+ (author!=null?author:"")+(status!=null?status:"");
        try {
            List<BookResponseDTO> cachedResponse = (List<BookResponseDTO>) redisTemplate.opsForValue().get(redisKey);
            if (cachedResponse != null) {
                return cachedResponse;
            }
        }catch(RedisConnectionFailureException e){
            System.out.println("WARNING - Client is not able to connect to redis cache, falling back to DB");
        }

        List<Book> books = bookRepository.findAll();

        List<BookResponseDTO> databaseResponse= books.stream()
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

        redisTemplate.opsForValue().set(redisKey,databaseResponse,ttl);
        return databaseResponse;
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        String redisKey= BOOK_KEY +id;
        try {
            BookResponseDTO cachedBook = (BookResponseDTO) redisTemplate.opsForValue().get(redisKey);
            if (cachedBook != null) {
                return cachedBook;
            }
        }catch(RedisConnectionFailureException e){
            System.out.println("WARNING - Client is not able to connect to redis cache, falling back to DB");
        }

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        BookResponseDTO response= bookMapper.toDTO(book);
        try {
            redisTemplate.opsForValue().set(redisKey, response, ttl);
        }catch(RedisConnectionFailureException e){
            System.out.println("Could not cache data in redis right now");
        }
        return response;
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO updatedBookDTO) {
        String redisKey=BOOK_KEY+id;

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(updatedBookDTO.getTitle());
        existingBook.setAuthor(updatedBookDTO.getAuthor());
        existingBook.setIsbn(updatedBookDTO.getIsbn());
        existingBook.setPublishedDate(updatedBookDTO.getPublishedDate());
        existingBook.setStatus(updatedBookDTO.getStatus());

        Book updated = bookRepository.save(existingBook);
        BookResponseDTO response= bookMapper.toDTO(updated);

        redisTemplate.opsForValue().set(redisKey,response,ttl);
        redisTemplate.delete(redisTemplate.keys(BOOK_LIST_KEY+"*"));
        return response;
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
        redisTemplate.delete(BOOK_KEY + id);
        redisTemplate.delete(redisTemplate.keys(BOOK_LIST_KEY + "*"));
    }

    public List<BookResponseDTO> getBooksPublishedAfter(LocalDate date) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getPublishedDate() != null && book.getPublishedDate().isAfter(date))
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void saveBooksFromCsv(InputStream inputStream){
        List<Book> books=new CsvHelper().parseCsvToBooks(inputStream);
        bookRepository.saveAll(books);
    }
}
