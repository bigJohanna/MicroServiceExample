package se.iths.bigjohanna.myservice;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void sendRealHttpRequestWithPost() throws Exception{
        User user = new User (0,"testUser", "testName", "testCity", 10f, true);
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":4,\"userName\":\"testUser\",\"realName\":\"testName\",\"city\":\"testCity\",\"income\":10,\"inRelationship\":true}"));
        // TODO: 2020-03-03 eftersom jag redan laddat databasen med tre objekt kommer min post bli id 4. Egentligen skiter jag ju i vilket id den får..Så jag kanske bara ska kolla stickprov på min json? 
    }

    //put
    //patch
    //delete
    //getOne
    //getAll

}

