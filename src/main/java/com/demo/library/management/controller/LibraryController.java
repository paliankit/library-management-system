package com.demo.library.management.controller;

import com.demo.library.management.dto.BookRequestDTO;
import com.demo.library.management.dto.BookResponseDTO;
import com.demo.library.management.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(libraryService.getAllBooksFiltered(author, status));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getBookById(id));
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO bookRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(libraryService.createBook(bookRequest));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO updatedBook
    ) {
        return ResponseEntity.ok(libraryService.updateBook(id, updatedBook));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        libraryService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/publishedAfter")
    public ResponseEntity<List<BookResponseDTO>> getBooksPublishedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(libraryService.getBooksPublishedAfter(date));
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFromCsv(@RequestParam("file") MultipartFile file){
         String type=file.getContentType();
         if(!Objects.equals(type,"text/csv")){
             return ResponseEntity.badRequest().body("Only csv files are supported !!!");
         }
         try{
             libraryService.saveBooksFromCsv(file.getInputStream());
             return ResponseEntity.ok().body("Books imported successfully !");
         }catch(Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Importing csv: " + e.getMessage());
         }
    }
}
