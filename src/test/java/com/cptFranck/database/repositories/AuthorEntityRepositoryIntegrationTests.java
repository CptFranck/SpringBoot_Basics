package com.cptFranck.database.repositories;

import com.cptFranck.database.TestDataUtil;
import com.cptFranck.database.domain.entity.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest){
        this.underTest = underTest;
    }

    @Test
    public void testAuthorCanBeCreated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testMultipleAuthorsCanBeCreated(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorEntityB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorEntityC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result).hasSize(3)
                .contains(authorEntityA, authorEntityB, authorEntityC);
    }

    @Test
    public void testAuthorsCanBeUpdated(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorEntityA);
        authorEntityA.setName("UPDATED");
        underTest.save(authorEntityA);

        Optional<AuthorEntity> result = underTest.findById(authorEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntityA);
    }

    @Test
    public void testAuthorsCanBeDeleted(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorEntityA);
        underTest.deleteById(authorEntityA.getId());

        Optional<AuthorEntity> result = underTest.findById(authorEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetAuthorsByAgeLessThan(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorEntityB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorEntityC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);
        assertThat(result).contains(authorEntityB, authorEntityC);
    }

    @Test
    public void testGetAuthorsByAgeGreaterThan(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorEntityB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorEntityC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgeGreaterThan(50);
        assertThat(result).contains(authorEntityA);
    }
}
