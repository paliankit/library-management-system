package com.demo.service;

import com.demo.model.Book;
import com.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    public void createBook(Book book){
        bookRepository.save(book);
    }
}
