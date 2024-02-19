package com.cptFranck.database.controller;

import com.cptFranck.database.TestDataUtil;
import com.cptFranck.database.domain.dto.BookDto;
import com.cptFranck.database.domain.entity.BookEntity;
import com.cptFranck.database.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private final BookService bookService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService){
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void TestCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void TestCreateBookReturnsHSaveBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDto.getAuthorDto())
        );
    }

    @Test
    public void TestReadBooksSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestReadAuthorReturnsListOfAuthors() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        this.bookService.save(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(bookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].author").value(bookEntity.getAuthorEntity())
        );
    }

    @Test
    public void TestReadOneBookSuccessfullyReturnsHttp200WhenBookExist() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        this.bookService.save(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestReadOneBookSuccessfullyReturnsHttp404WhenBookDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-6789-99999999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void TestReadOneBookReturnsAuthorWhenBookExist() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        this.bookService.save(bookEntity.getIsbn(), bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookEntity.getAuthorEntity())
        );
    }

    @Test
    public void TestFullUpdateBookSuccessfullyReturnsHttp200WhenBookAlreadyExist() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        BookEntity savedBook = this.bookService.save(bookEntity.getIsbn(), bookEntity);
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String BookDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestFullUpdateBooksReturnsBookWhenAuthorAlreadyExist() throws Exception {
        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(null);
        BookEntity savedBookA = this.bookService.save(bookEntityA.getIsbn(), bookEntityA);
        BookDto bookDtoB = TestDataUtil.createTestBookDtaB(null);
        String BookDtoBJson = objectMapper.writeValueAsString(bookDtoB);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BookDtoBJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDtoB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDtoB.getAuthorDto())
        );
    }

    @Test
    public void TestPartialUpdateBookSuccessfullyReturnsHttp200WhenBookAlreadyExist() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        BookEntity savedBook = this.bookService.save(bookEntity.getIsbn(), bookEntity);
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setTitle("UPDATED");
        String BookDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestPartialUpdateBookSuccessfullyReturnsHttp404WhenBooksDoesNotExist() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/978-1-2345-6789-99999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void TestPartialUpdateBooksReturnsBookWhenAuthorAlreadyExist() throws Exception {
        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(null);
        BookEntity savedBookA = this.bookService.save(bookEntityA.getIsbn(), bookEntityA);
        BookDto bookDtoA = TestDataUtil.createTestBookDtoA(null);
        bookDtoA.setTitle("UPDATED");
        String BookDtoBJson = objectMapper.writeValueAsString(bookDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BookDtoBJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDtoA.getAuthorDto())
        );
    }

    @Test
    public void TestDeleteAuthorSuccessfullyReturnsHttp204WhenAuthorExists() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        this.bookService.save(bookEntity.getIsbn(), bookEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void TestDeleteAuthorSuccessfullyReturnsHttp404WhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/978-1-2345-6789-99999999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
