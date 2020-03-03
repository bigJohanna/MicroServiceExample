package se.iths.bigjohanna.myservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UsersController.class)
@Import({UsersModelAssembler.class})
public class UsersControllerClassTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsersRepository repository;

    @BeforeEach
    void setup(){
        when(repository.findById(1)).thenReturn(Optional.of(new User(1, "testU", "testName", "testCity", 10, true)));
        /*when(repository.save(any(User.class))).thenAnswer(invocationMock ->{
            Object[] args = invocationMock.getArguments();
            var u = (User) args[0];
            return new User(1,u.getUserName(),u.getRealName(),u.getCity(),u.getIncome(),u.isInRelationship());
        });*/
    }

    @Test
    @DisplayName("Calls GET method with url /api/v1/users/1")
    void getOnePersonWithValidIdOne() throws Exception {
        mockMvc.perform(
                get("/api/v1/users/1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/users/1")));
    }
}
