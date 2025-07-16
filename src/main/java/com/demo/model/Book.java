package com.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String title;

    private String author;

    private String isbn;

    private LocalDate publishedDate;


}
