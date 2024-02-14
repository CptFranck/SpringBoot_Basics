package com.cptFranck.database.dao.impl;

import com.cptFranck.database.dao.TestDataUtil;
import com.cptFranck.database.domain.Author;
import com.cptFranck.database.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookDaoImpl underTest;

    @Test
    public void testCreateBook(){
        Book book = TestDataUtil.createTestBookA();
        underTest.create(book);

        verify(jdbcTemplate).update(
                eq("INSERT INTO books (isbn, title, author_id) VALUES (?,?,?)"),
                eq("978-1-2345-6789-0"),
                eq("The Shadow in the Attic"),
                eq(1L)
            );
    }

    @Test
    public void testFindOneGenerateCorrectSql(){
        underTest.findOne("978-1-2345-6789-0");
        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id FROM books WHERE isbn = ? LIMIT 1"),
                ArgumentMatchers.<AuthorDaoImpl.AuthorRowMapper>any(),
                eq("978-1-2345-6789-0")
        );
    }

    @Test
    public void testFindManyGenerateCorrectSql(){
        underTest.findMany();
        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id FROM books"),
                ArgumentMatchers.<AuthorDaoImpl.AuthorRowMapper>any()
        );
    }

    @Test
    public void testUpdateGenerateCorrectSql(){
        Book book = TestDataUtil.createTestBookA();
        underTest.update(book.getIsbn(), book);
        verify(jdbcTemplate).update(
                "UPDATE books SET isbn = ?, title = ?, author_id = ? WHERE isbn = ?",
                "978-1-2345-6789-0",
                "The Shadow in the Attic",
                1L,
                "978-1-2345-6789-0"
        );
    }

    @Test
    public void testDeleteGenerateCorrectSql(){
        underTest.delete("978-1-2345-6789-0");
        verify(jdbcTemplate).update(
                "DELETE FROM books WHERE isbn = ?",
                "978-1-2345-6789-0"
        );
    }
}
