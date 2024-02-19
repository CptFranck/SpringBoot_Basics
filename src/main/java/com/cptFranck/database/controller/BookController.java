package com.cptFranck.database.controller;

import com.cptFranck.database.domain.dto.BookDto;
import com.cptFranck.database.domain.entity.BookEntity;
import com.cptFranck.database.mappers.Mapper;
import com.cptFranck.database.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;
    private final Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper){
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean bookExists = bookService.isExist(isbn);
        BookEntity savedBookDta = bookService.save(isbn, bookEntity);
        if(bookExists){
            return new ResponseEntity<>(bookMapper.mapTo(savedBookDta), HttpStatus.OK);
        }
        return new ResponseEntity<>(bookMapper.mapTo(savedBookDta), HttpStatus.CREATED);
    }

    @GetMapping(path = "/books")
    public List<BookDto> readBooks(){
        List<BookEntity> bookEntities = bookService.findAll();
        return bookEntities.stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/books")
    public Page<BookDto> readBooksPageable(Pageable pageable){
        Page<BookEntity> bookEntities = bookService.findAll(pageable);
        return bookEntities.map(bookMapper::mapTo);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> readOneBook(@PathVariable("isbn") String isbn){
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateAuthor(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        if(!bookService.isExist(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity savedBookEntity = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteAuthor(@PathVariable("isbn") String isbn){
        if(!bookService.isExist(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
