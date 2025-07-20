package com.demo.library.management.kafka;

import com.demo.library.management.model.Book;
import com.demo.library.management.repository.BookRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookConsumer {

    private final BookRepository bookRepository;

    public BookConsumer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @KafkaListener(topics = "books", groupId = "book-consumer-group", containerFactory = "bookKafkaListenerFactory")
    public void consume(Book book) {
        System.out.println("Received book: " + book.getTitle());
        bookRepository.save(book);
    }
}
