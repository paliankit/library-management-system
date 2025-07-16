package com.demo.controller;

import com.demo.model.Book;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

    @PostMapping("/create")
    public void createBook(@RequestBody Book book){

    }
}
