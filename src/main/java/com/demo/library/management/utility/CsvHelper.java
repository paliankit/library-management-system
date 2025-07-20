package com.demo.library.management.utility;

import com.demo.library.management.model.Book;
import com.demo.library.management.model.BookStatus;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvHelper {

    public List<Book> parseCsvToBooks(InputStream inputStream){
        List<Book> books=new ArrayList<Book>();
        try{
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            CSVReader csvReader=new CSVReader(bufferedReader);
            String[] header=csvReader.readNext();
            String[] line;
            while((line=csvReader.readNext())!=null){
                Book book=new Book();
                book.setTitle(line[0]);
                book.setAuthor(line[1]);
                book.setIsbn(line[2]);
                book.setPublishedDate(LocalDate.parse(line[3]));
                book.setStatus(BookStatus.valueOf(line[4]));
                books.add(book);
            }
        }catch(Exception e){
            throw new RuntimeException("Failed to parse csv file: "+ e.getMessage());
        }
        return books;
    }
}
