package com.cptFranck.database.repositories;

import com.cptFranck.database.domain.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, String>,
                                        PagingAndSortingRepository<BookEntity, String> {
}
