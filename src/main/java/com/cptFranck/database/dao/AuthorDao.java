package com.cptFranck.database.dao;

import com.cptFranck.database.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    void create(Author author);

    Optional<Author> findOne(long authorId);

    List<Author> findMany();

    void update(long id, Author author);

    void delete(Long id);
}
