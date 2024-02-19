package com.cptFranck.database.controller;

import com.cptFranck.database.TestDataUtil;
import com.cptFranck.database.domain.dto.AuthorDto;
import com.cptFranck.database.domain.entity.AuthorEntity;
import com.cptFranck.database.service.AuthorService;
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
public class AuthorControllerIntegrationTests {

    private final AuthorService authorService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService){
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void TestCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorDto authorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorDtoA.setId(null);
        String authorAJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void TestCreateAuthorReturnsSavedAuthor() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        authorDto.setId(null);
        String authorAJson = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorAJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void TestReadAuthorsSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestReadAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        this.authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(authorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(authorEntity.getAge())
        );
    }

    @Test
    public void TestReadOneAuthorSuccessfullyReturnsHttp200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        this.authorService.save(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestReadOneAuthorSuccessfullyReturnsHttp404WhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/999999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void TestReadOneAuthorReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        this.authorService.save(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
        );
    }

    @Test
    public void TestFullUpdateAuthorSuccessfullyReturnsHttp200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = this.authorService.save(authorEntity);
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestFullUpdateAuthorSuccessfullyReturnsHttp404WhenAuthorDoesNotExist() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void TestFullUpdateAuthorReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = this.authorService.save(authorEntityA);
        AuthorDto authorDtoB = TestDataUtil.createTestAuthorDtoB();
        String authorDtoBJson = objectMapper.writeValueAsString(authorDtoB);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoBJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDtoB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDtoB.getAge())
        );
    }

    @Test
    public void TestPartialUpdateAuthorSuccessfullyReturnsHttp200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = this.authorService.save(authorEntityA);
        AuthorDto authorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorDtoA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(authorDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void TestPartialUpdateAuthorSuccessfullyReturnsHttp404WhenAuthorDoesNotExist() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void TestPartialUpdateAuthorReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = this.authorService.save(authorEntityA);
        AuthorDto authorDtoA = TestDataUtil.createTestAuthorDtoA();
        authorDtoA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(authorDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDtoA.getAge())
        );
    }

    @Test
    public void TestDeleteAuthorSuccessfullyReturnsHttp204WhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        this.authorService.save(authorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void TestDeleteAuthorSuccessfullyReturnsHttp404WhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
