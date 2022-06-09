package com.backend.springboot.docker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.springboot.docker.controllers.PersonController;
import com.backend.springboot.docker.services.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootDockerApp.class)
public class PersonRestControllerTest {

    MockMvc mockMvc;
    @Mock
    private PersonService personService;
    @InjectMocks
    private PersonController personController;


    @Test
    public void requestBodyTest() {
        try {
            mockMvc.perform(post("/people")
                            .content("")
                            .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

}
