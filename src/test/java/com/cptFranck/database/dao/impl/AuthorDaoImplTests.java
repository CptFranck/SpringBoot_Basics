package com.cptFranck.database.dao.impl;

import com.cptFranck.database.dao.TestDataUtil;
import com.cptFranck.database.domain.Author;
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
public class AuthorDaoImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuthorDaoImpl underTest;

    @Test
    public void testCreateAuthor(){
        Author author = TestDataUtil.createTestAuthorA();
        underTest.create(author);

        verify(jdbcTemplate).update(
                eq("INSERT INTO authors (id, name, age) VALUES (?,?,?)"),
                eq(1L),
                eq("Abigail Rose"),
                eq(80)
            );
    }

    @Test
    public void testFindOneGenerateCorrectSql(){
        underTest.findOne(1L);
        verify(jdbcTemplate).query(
                eq("SELECT id, name, age FROM authors WHERE id = ? LIMIT 1"),
                ArgumentMatchers.<AuthorDaoImpl.AuthorRowMapper>any(),
                eq(1L)
        );
    }

    @Test
    public void testFindManyGenerateCorrectSql(){
        underTest.findMany();
        verify(jdbcTemplate).query(
                eq("SELECT id, name, age FROM authors"),
                ArgumentMatchers.<AuthorDaoImpl.AuthorRowMapper>any()
        );
    }

    @Test
    public void testUpdateGenerateCorrectSql(){
        Author author = TestDataUtil.createTestAuthorA();
        underTest.update(author.getId(), author);
        verify(jdbcTemplate).update(
                "UPDATE authors SET id = ?, name = ?, age = ? WHERE id = ?",
                1L,
                "Abigail Rose",
                80,
                1L
        );
    }

    @Test
    public void testDeleteGenerateCorrectSql(){
        underTest.delete(1L);
        verify(jdbcTemplate).update(
                "DELETE FROM authors WHERE id = ?",
                1L
        );
    }
}
