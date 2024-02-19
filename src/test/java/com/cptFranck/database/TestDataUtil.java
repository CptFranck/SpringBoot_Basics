package com.cptFranck.database;

import com.cptFranck.database.domain.dto.AuthorDto;
import com.cptFranck.database.domain.dto.BookDto;
import com.cptFranck.database.domain.entity.AuthorEntity;
import com.cptFranck.database.domain.entity.BookEntity;

public final class TestDataUtil {

    public static AuthorEntity createTestAuthorEntityA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }
    public static AuthorEntity createTestAuthorEntityB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Thomas Cronin")
                .age(44)
                .build();
    }

    public static AuthorDto createTestAuthorDtoB() {
        return AuthorDto.builder()
                .id(2L)
                .name("Thomas Cronin")
                .age(44)
                .build();
    }

    public static AuthorEntity createTestAuthorEntityC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Jesse A Cassey")
                .age(24)
                .build();
    }

    public static BookEntity createTestBookEntityA(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtoA(AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow in the Attic")
                .authorDto(authorDto)
                .build();
    }

    public static BookEntity createTestBookEntityB(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-1")
                .title("The Shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtaB(AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-1")
                .title("The Shadow in the Attic")
                .authorDto(authorDto)
                .build();
    }

    public static BookEntity createTestBookEntityC(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtoC(AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Shadow in the Attic")
                .authorDto(authorDto)
                .build();
    }
}
