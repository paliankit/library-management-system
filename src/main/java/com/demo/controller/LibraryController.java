package com.demo.controller;

import com.demo.model.Book;
import com.demo.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody @Valid Book book){
        System.out.println("Received book: " + book);
         libraryService.createBook(book);
         return ResponseEntity.status(HttpStatus.CREATED).body("Book created successfully");
    }
}
