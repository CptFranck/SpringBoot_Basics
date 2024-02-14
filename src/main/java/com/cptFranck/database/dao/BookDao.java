package com.cptFranck.database.dao;

import com.cptFranck.database.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    void create(Book book);

    Optional<Book> findOne(String BookIsbn);

    List<Book> findMany();

    void update(String isbn, Book book);

    void delete(String isbn);
}
