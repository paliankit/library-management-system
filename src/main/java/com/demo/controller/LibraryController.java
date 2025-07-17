package com.demo.controller;

import com.demo.model.Book;
import com.demo.service.LibraryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/libraryManagement")
public class LibraryController {

    @Autowired
    private LibraryServiceImpl libraryService;

    @PostMapping("/createBook")
    public void createBook(@RequestBody Book book){
         libraryService.createBook(book);
    }
}
