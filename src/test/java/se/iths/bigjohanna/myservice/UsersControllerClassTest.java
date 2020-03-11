package se.iths.bigjohanna.myservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.*;

@WebMvcTest(UsersController.class)
@Import({UsersModelAssembler.class})
public class UsersControllerClassTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsersRepository repository;

    @BeforeEach
    void setup() {
        when(repository.findById(1)).thenReturn(Optional.of(new User(1, "Annis", "Anna Alm", "Ahlborg", 123f, true)));
        when(repository.existsById(1)).thenReturn(true);
        when(repository.save(any(User.class))).thenAnswer(invocationMock -> {
            Object[] args = invocationMock.getArguments();
            var u = (User) args[0];
            return new User(1, u.getUserName(), u.getRealName(), u.getCity(), u.getIncome(), u.getInRelationship());
        });
        when(repository.findAll()).thenReturn(List.of(new User(1, "Annis", "Anna Alm", "Ahlborg", 10000f, true),
                new User(2, "Sebbe", "Sebastian Waltilla", "Fjärås", 60000f, true),
                new User(3, "Jonte", "Jonathan Holm", "Umeå", 40000f, false)));
    }

    @Test
    @DisplayName("Calls GET method with url /api/v1/users/1")
    void getOnePersonWithValidIdOne() throws Exception {
        mockMvc.perform(
                get("/api/v1/users/1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/users/1")));
    }

    //Testa getAll
    @Test
    @DisplayName("Calls GET method with url /api/v1/users")
    void getAllUsers() throws Exception {
        mockMvc.perform(
                get("/api/v1/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/users")));
    }

    //Testa get one user
    @Test
    @DisplayName("Calls GET with invalid ID with url /api/v1/users/99")
    void getUserWithInvalidID() throws Exception {
        mockMvc.perform(get("/api/v1/users/99")).andExpect(status().isNotFound());
    }

    //Testa Patch
    @Test
    @DisplayName("Calls PATCH to modify RealName with url /api/v1/users/1")
    void modifyOnlyCertainFieldOnUser() throws Exception {
        mockMvc.perform(patch(("/api/v1/users/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"realName\":\"Jo Svall\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("realName", is("Jo Svall")))
                .andExpect(jsonPath("userName", is("Annis")));
    }


    //Testa POST
    @Test
    @DisplayName("Calls Post method to add new User to database")
    void addNewUserToDatabase() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType("application/json")
                .content("{\"id\":0,\"realName\":\"Sofia Kall\",\"userName\":\"bigTest\",\"city\":\"Lund\",\"inRelationship\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("_links.users.href", is("http://localhost/api/v1/users")));
    }

    //Delete
    @Test
    @DisplayName("Removes user with ID 1")
    void removeUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1")
                .accept("application/json"))
                .andExpect(status().isOk());
    }

    //Put
    @Test
    @DisplayName("Replace all information on user")
    void replaceEntireUserKeepId() throws Exception {
        mockMvc.perform(
                put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0,\"userName\":\"bigTest\",\"realName\":\"Sofia Kall\",\"city\":\"Lund\",\"inRelationship\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("realName", is("Sofia Kall")))
                .andExpect(jsonPath("income", is(nullValue())));
    }
}
