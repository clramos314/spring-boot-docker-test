package com.backend.springboot.docker;

import com.backend.springboot.docker.models.entity.Job;
import com.backend.springboot.docker.models.entity.Person;
import com.backend.springboot.docker.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /people success")
    void testGetPeopleSuccess() throws Exception {
        // Setup our mocked service
        Job job = new Job();
        job.setId(1);
        Person person1 = new Person(1, "Tomas Wolf", "2013-08-11", job);
        Person person2 = new Person(2, "Olivia Newton", "2015-02-20", job);
        doReturn(Lists.newArrayList(person1, person2)).when(service).findAll();

        // Execute the GET request
        mockMvc.perform(get("/people"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tomas Wolf")))
                .andExpect(jsonPath("$[0].entryDate", is(LocalDate.parse("2013-08-11").toString())))
                .andExpect(jsonPath("$[0].job.id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Olivia Newton")))
                .andExpect(jsonPath("$[1].entryDate", is(LocalDate.parse("2015-02-20").toString())))
                .andExpect(jsonPath("$[1].job.id", is(1)));
    }

    @Test
    @DisplayName("GET /people/1 success")
    void testGetPersonById() throws Exception {
        // Setup our mocked service
        Job job = new Job();
        job.setId(3);
        Person person = new Person(10, "Richard Hamilton", "2013-01-21", job);
        doReturn(Optional.of(person)).when(service).findById(10);

        // Execute the GET request
        mockMvc.perform(get("/people/{id}", 10))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("Richard Hamilton")))
                .andExpect(jsonPath("$.entryDate", is("2013-01-21")))
                .andExpect(jsonPath("$.job.id", is(3)));
    }

    @Test
    @DisplayName("GET /people/1 - Not Found")
    void testGetPersonByIdNotFound() throws Exception {
        // Setup our mocked service
        doReturn(Optional.empty()).when(service).findById(1);

        // Execute the GET request
        mockMvc.perform(get("/people/{id}", 1))
                // Validate the response code
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /people success")
    void testCreatePerson() throws Exception {
        // Setup our mocked service
        Job job = new Job();
        job.setId(3);
        Person personToPost = new Person(12, "Michelle Smith", "2017-07-01", job);
        Person personToReturn = new Person(12, "Michelle Smith", "2017-07-01", job);
        doReturn(personToReturn).when(service).save(any());

        // Execute the POST request
        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personToPost)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(12)))
                .andExpect(jsonPath("$.name", is("Michelle Smith")))
                .andExpect(jsonPath("$.entryDate", is("2017-07-01")))
                .andExpect(jsonPath("$.job.id", is(3)));
    }

    @Test
    @DisplayName("PUT /people/1 success")
    void testUpdatePerson() throws Exception {
        // Setup our mocked service
        Job job = new Job();
        job.setId(5);
        Person personToPut = new Person(12, "Robert King", "2014-07-25", job);
        Person personToReturnFindBy = new Person(12, "Robert King", "2014-07-25", job);
        Person personToReturnSave = new Person(12, "Robert Queen", "2014-03-23", job);
        doReturn(Optional.of(personToReturnFindBy)).when(service).findById(12);
        doReturn(personToReturnSave).when(service).save(any());

        // Execute the PUT request
        mockMvc.perform(put("/people/{id}", 12)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 2)
                        .content(asJsonString(personToPut)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(12)))
                .andExpect(jsonPath("$.name", is("Robert Queen")))
                .andExpect(jsonPath("$.entryDate", is("2014-03-23")))
                .andExpect(jsonPath("$.job.id", is(5)));
    }

    @Test
    @DisplayName("PUT /people/102 - Not Found")
    void testUpdatePersonNotFound() throws Exception {
        // Setup our mocked service
        Job job = new Job();
        job.setId(1);
        Person personToPut = new Person(102, "Charles Baker", "2014-07-25", job);
        doReturn(Optional.empty()).when(service).findById(102);

        // Execute the POST request
        mockMvc.perform(put("/people/{id}", 102)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 3)
                        .content(asJsonString(personToPut)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /people/1 success")
    void testDeletePerson() throws Exception {
        // Setup our mocked service
        doNothing().when(service).deleteById(1);

        // Execute the DELETE request
        mockMvc.perform(delete("/people/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 2))

                // Validate the response code and content type
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /people/filter-by-salary/25000 success")
    void testGetPeopleFilteringBySalaryLimitSuccess() throws Exception {
        // Setup our mocked service
        Job job1 = new Job();
        job1.setId(1);
        job1.setSalary(25000.0);
        Job job2 = new Job();
        job2.setId(5);
        job2.setSalary(20000.0);
        Person person1 = new Person(1, "Tomas Wolf", "2013-08-11", job1);
        Person person2 = new Person(3, "Phillip Garland", "2015-01-21", job2);
        doReturn(Lists.newArrayList(person1, person2)).when(service).findBySalary(25000.0);

        // Execute the GET request
        mockMvc.perform(get("/people/filter-by-salary/{limit}", 25000))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tomas Wolf")))
                .andExpect(jsonPath("$[0].entryDate", is(LocalDate.parse("2013-08-11").toString())))
                .andExpect(jsonPath("$[0].job.id", is(1)))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].name", is("Phillip Garland")))
                .andExpect(jsonPath("$[1].entryDate", is(LocalDate.parse("2015-01-21").toString())))
                .andExpect(jsonPath("$[1].job.id", is(5)));
    }

    @Test
    @DisplayName("GET /people/filter-between-dates/2013-08-11/2015-01-21 success")
    void testGetPeopleFilteringBetweenStartDateAndEndDateSuccess() throws Exception {
        // Setup our mocked service
        Job job1 = new Job();
        job1.setId(1);
        Job job2 = new Job();
        job2.setId(5);
        Person person1 = new Person(1, "Tomas Wolf", "2013-08-11", job1);
        Person person2 = new Person(3, "Phillip Garland", "2015-01-21", job2);
        doReturn(Lists.newArrayList(person1, person2)).when(service).findBetweenStartDateAndEndDate(LocalDate.parse("2013-08-11"), LocalDate.parse("2015-01-21"));

        // Execute the GET request
        mockMvc.perform(get("/people/filter-between-dates/{start_date}/{end_date}", "2013-08-11", "2015-01-21"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tomas Wolf")))
                .andExpect(jsonPath("$[0].entryDate", is(LocalDate.parse("2013-08-11").toString())))
                .andExpect(jsonPath("$[0].job.id", is(1)))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].name", is("Phillip Garland")))
                .andExpect(jsonPath("$[1].entryDate", is(LocalDate.parse("2015-01-21").toString())))
                .andExpect(jsonPath("$[1].job.id", is(5)));
    }

    static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
