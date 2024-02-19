package com.cptFranck.database.repositories;

import com.cptFranck.database.TestDataUtil;
import com.cptFranck.database.domain.entity.AuthorEntity;
import com.cptFranck.database.domain.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private final BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest){
        this.underTest = underTest;
    }

    @Test
    public void testBookCanBeCreated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testMultipleBooksCanBeCreated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntityA);
        BookEntity bookEntityB = TestDataUtil.createTestBookEntityB(authorEntity);
        underTest.save(bookEntityB);
        BookEntity bookEntityC = TestDataUtil.createTestBookEntityC(authorEntity);
        underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result).hasSize(3).contains(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void testBooksCanBeUpdated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntityA);

        bookEntityA.setTitle("UPDATED");
        underTest.save(bookEntityA);

        Optional<BookEntity> result = underTest.findById(bookEntityA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntityA);
    }

    @Test
    public void testBooksCanBeDeleted(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntity);
        underTest.deleteById(bookEntity.getIsbn());

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isEmpty();
    }


}
