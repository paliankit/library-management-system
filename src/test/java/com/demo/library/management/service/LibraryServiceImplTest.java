package com.demo.library.management.service;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.exception.BookNotFoundException;
import com.demo.library.management.mapper.BookMapper;
import com.demo.library.management.model.Book;
import com.demo.library.management.model.BookStatus;
import com.demo.library.management.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceImplTest {

    private LibraryServiceImpl libraryService;

    private BookRepository bookRepository;
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookMapper = mock(BookMapper.class);
        libraryService = new LibraryServiceImpl();

        ReflectionTestUtils.setField(libraryService, "bookRepository", bookRepository);
        ReflectionTestUtils.setField(libraryService, "bookMapper", bookMapper);
    }

    @Test
    void createBook_ShouldReturnDTO_WhenBookSaved() {
        BookRequestDTO dto = new BookRequestDTO("Title", "Author", "ISBN123", LocalDate.now(), BookStatus.AVAILABLE);
        Book entity = new Book(null, "Title", "Author", "ISBN123", LocalDate.now(), BookStatus.AVAILABLE);
        Book saved = new Book(1L, "Title", "Author", "ISBN123", LocalDate.now(), BookStatus.AVAILABLE);
        BookResponseDTO responseDTO = new BookResponseDTO(1L, "Title", "Author", "ISBN123", LocalDate.now(), BookStatus.AVAILABLE);

        when(bookMapper.toEntity(dto)).thenReturn(entity);
        when(bookRepository.save(entity)).thenReturn(saved);
        when(bookMapper.toDTO(saved)).thenReturn(responseDTO);

        BookResponseDTO result = libraryService.createBook(dto);

        assertEquals(responseDTO, result);
        verify(bookMapper).toEntity(dto);
        verify(bookRepository).save(entity);
        verify(bookMapper).toDTO(saved);
    }

    @Test
    void getAllBooksFiltered_ShouldFilterByAuthorAndStatus() {
        Book book1 = new Book(1L, "Title1", "John Doe", "ISBN1", LocalDate.now(), BookStatus.AVAILABLE);
        Book book2 = new Book(2L, "Title2", "Jane Doe", "ISBN2", LocalDate.now(), BookStatus.BORROWED);
        BookResponseDTO dto1 = new BookResponseDTO(1L, "Title1", "John Doe", "ISBN1", LocalDate.now(), BookStatus.AVAILABLE);

        List<Book> allBooks = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(allBooks);
        when(bookMapper.toDTO(book1)).thenReturn(dto1);

        List<BookResponseDTO> filtered = libraryService.getAllBooksFiltered("John Doe", "AVAILABLE");

        assertEquals(1, filtered.size());
        assertEquals(dto1, filtered.get(0));
    }

    @Test
    void getAllBooksFiltered_ShouldReturnEmptyList_WhenInvalidStatus() {
        Book book = new Book(1L, "Title", "Author", "ISBN", LocalDate.now(), BookStatus.AVAILABLE);
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        List<BookResponseDTO> result = libraryService.getAllBooksFiltered(null, "INVALID_STATUS");

        assertTrue(result.isEmpty());
    }

    @Test
    void getBookById_ShouldReturnDTO_WhenBookExists() {
        Book book = new Book(1L, "Title", "Author", "ISBN", LocalDate.now(), BookStatus.AVAILABLE);
        BookResponseDTO dto = new BookResponseDTO(1L, "Title", "Author", "ISBN", LocalDate.now(), BookStatus.AVAILABLE);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(dto);

        BookResponseDTO result = libraryService.getBookById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getBookById_ShouldThrow_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> libraryService.getBookById(1L));
        assertEquals("Book not found with id: 1", ex.getMessage());
    }

    @Test
    void updateBook_ShouldReturnUpdatedDTO_WhenBookExists() {
        BookRequestDTO updateDto = new BookRequestDTO("New Title", "New Author", "ISBN2", LocalDate.now(), BookStatus.BORROWED);
        Book existingBook = new Book(1L, "Old Title", "Old Author", "ISBN1", LocalDate.now().minusDays(10), BookStatus.AVAILABLE);
        Book updatedBook = new Book(1L, "New Title", "New Author", "ISBN2", LocalDate.now(), BookStatus.BORROWED);
        BookResponseDTO updatedDTO = new BookResponseDTO(1L, "New Title", "New Author", "ISBN2", LocalDate.now(), BookStatus.BORROWED);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDTO(updatedBook)).thenReturn(updatedDTO);

        BookResponseDTO result = libraryService.updateBook(1L, updateDto);

        assertEquals(updatedDTO, result);
        assertEquals("New Title", existingBook.getTitle());
        assertEquals("New Author", existingBook.getAuthor());
        assertEquals("ISBN2", existingBook.getIsbn());
        assertEquals(BookStatus.BORROWED, existingBook.getStatus());
    }

    @Test
    void updateBook_ShouldThrow_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookRequestDTO updateDto = new BookRequestDTO("Title", "Author", "ISBN", LocalDate.now(), BookStatus.AVAILABLE);

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> libraryService.updateBook(1L, updateDto));
        assertEquals("Book not found with id: 1", ex.getMessage());
    }

    @Test
    void deleteBook_ShouldDelete_WhenBookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        libraryService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldThrow_WhenBookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> libraryService.deleteBook(1L));
        assertEquals("Book not found with id: 1", ex.getMessage());
    }

    @Test
    void getBooksPublishedAfter_ShouldReturnFilteredList() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        Book book1 = new Book(1L, "Title1", "Author1", "ISBN1", LocalDate.of(2021, 1, 1), BookStatus.AVAILABLE);
        Book book2 = new Book(2L, "Title2", "Author2", "ISBN2", LocalDate.of(2019, 1, 1), BookStatus.AVAILABLE);
        BookResponseDTO dto1 = new BookResponseDTO(1L, "Title1", "Author1", "ISBN1", LocalDate.of(2021, 1, 1), BookStatus.AVAILABLE);

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));
        when(bookMapper.toDTO(book1)).thenReturn(dto1);

        List<BookResponseDTO> result = libraryService.getBooksPublishedAfter(date);

        assertEquals(1, result.size());
        assertEquals(dto1, result.get(0));
    }
}