package com.cptFranck.database.dao.impl;

import com.cptFranck.database.dao.AuthorDao;
import com.cptFranck.database.dao.TestDataUtil;
import com.cptFranck.database.domain.Author;
import com.cptFranck.database.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookDaoImplIntegrationTests {

    private final AuthorDao authorDao;

    private final BookDaoImpl underTest;

    @Autowired
    public BookDaoImplIntegrationTests(AuthorDao authorDao, BookDaoImpl underTest){
        this.authorDao = authorDao;
        this.underTest = underTest;
    }

    @Test
    public void testBookCanBeCreated(){
        Author author = TestDataUtil.createTestAuthorA();
        authorDao.create(author);
        Book book = TestDataUtil.createTestBookA();
        book.setAuthorId(author.getId());
        underTest.create(book);
        Optional<Book> result = underTest.findOne(book.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testMultipleBooksCanBeCreated(){
        Author author = TestDataUtil.createTestAuthorA();
        authorDao.create(author);

        Book bookA = TestDataUtil.createTestBookA();
        underTest.create(bookA);
        bookA.setAuthorId(author.getId());
        Book bookB = TestDataUtil.createTestBookB();
        bookB.setAuthorId(author.getId());
        underTest.create(bookB);
        Book bookC = TestDataUtil.createTestBookC();
        bookC.setAuthorId(author.getId());
        underTest.create(bookC);
        List<Book> result = underTest.findMany();
        assertThat(result).hasSize(3).containsExactly(bookA,bookB,bookC);
    }

    @Test
    public void testBooksCanBeUpdated(){
        Author author = TestDataUtil.createTestAuthorA();
        authorDao.create(author);
        Book bookA = TestDataUtil.createTestBookA();
        bookA.setAuthorId(author.getId());
        underTest.create(bookA);

        bookA.setTitle("UPDATED");
        underTest.update(bookA.getIsbn(), bookA);

        Optional<Book> result = underTest.findOne(bookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookA);
    }

    @Test
    public void testBooksCanBeDeleted(){
        Author author = TestDataUtil.createTestAuthorA();
        authorDao.create(author);
        Book book = TestDataUtil.createTestBookA();
        book.setAuthorId(author.getId());
        underTest.create(book);
        underTest.delete(book.getIsbn());

        Optional<Book> result = underTest.findOne(book.getIsbn());
        assertThat(result).isEmpty();
    }
}
