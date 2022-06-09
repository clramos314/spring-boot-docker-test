package com.backend.springboot.docker;

import com.backend.springboot.docker.models.entity.Job;
import com.backend.springboot.docker.models.entity.Person;
import com.backend.springboot.docker.models.repository.PersonRepository;
import com.backend.springboot.docker.services.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService service;

    @MockBean
    private PersonRepository repository;

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        // Setup our mock repository
        Job job = new Job();
        job.setId(1);
        Person person = new Person(1, "Tomas Wolf", "2013-08-11", job);
        doReturn(Optional.of(person)).when(repository).findById(1);

        // Execute the service call
        Optional<Person> returnedPerson = service.findById(1);

        // Assert the response
        Assertions.assertTrue(returnedPerson.isPresent(), "Person was not found");
        Assertions.assertSame(returnedPerson.get(), person, "The person returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup our mock repository
        doReturn(Optional.empty()).when(repository).findById(1);

        // Execute the service call
        Optional<Person> returnedPerson = service.findById(1);

        // Assert the response
        Assertions.assertFalse(returnedPerson.isPresent(), "Person should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        Job job = new Job();
        job.setId(1);
        Person person1 = new Person(1, "Tomas Wolf", "2013-08-11", job);
        Person person2 = new Person(2, "Olivia Newton", "2015-02-20", job);
        doReturn(Arrays.asList(person1, person2)).when(repository).findAll();

        // Execute the service call
        Iterable<Person> iterablePeople = service.findAll();
        List<Person> people = StreamSupport
                .stream(iterablePeople.spliterator(), false)
                .collect(Collectors.toList());

        // Assert the response
        Assertions.assertEquals(2, people.size(), "findAll should return 2 people");
    }

    @Test
    @DisplayName("Test save person")
    void testSave() {
        // Setup our mock repository
        Job job = new Job();
        job.setId(1);
        Person person = new Person(101, "Tomas Wolf", "2013-08-11", job);
        doReturn(person).when(repository).save(any());

        // Execute the service call
        Person returnedPerson = service.save(person);

        // Assert the response
        Assertions.assertNotNull(returnedPerson, "The saved person should not be null");
        Assertions.assertEquals(101, returnedPerson.getId(), "The id should be incremented");
    }

    @Test
    @DisplayName("Test delete person")
    void testDelete() {
        // Setup our mocked service
        doNothing().when(repository).deleteById(1);

        // Execute the service call
        service.deleteById(1);

        // Assert the response
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Test findBySalary Success")
    void testFindBySalary() {
        // Setup our mocked service
        Job job1 = new Job();
        job1.setId(1);
        job1.setSalary(25000.0);
        Job job2 = new Job();
        job2.setId(5);
        job2.setSalary(20000.0);
        Person person1 = new Person(1, "Tomas Wolf", "2013-08-11", job1);
        Person person2 = new Person(3, "Phillip Garland", "2015-01-21", job2);
        doReturn(Arrays.asList(person1, person2)).when(repository).findBySalary(25000.0);

        // Execute the service call
        List<Person> people = service.findBySalary(25000.0);

        // Assert the response
        Assertions.assertEquals(2, people.size(), "findBySalary should return 2 people");
    }

    @Test
    @DisplayName("Test findBetweenStartDateAndEndDate Success")
    void testFindBetweenStartDateAndEndDate() {
        // Setup our mocked service
        Job job1 = new Job();
        job1.setId(1);
        Job job2 = new Job();
        job2.setId(5);
        Person person1 = new Person(1, "Tomas Wolf", "2013-08-11", job1);
        Person person2 = new Person(3, "Phillip Garland", "2015-01-21", job2);
        doReturn(Arrays.asList(person1, person2)).when(repository).findBetweenStartDateAndEndDate(LocalDate.parse("2013-08-11"), LocalDate.parse("2015-01-21"));

        // Execute the service call
        List<Person> people = service.findBetweenStartDateAndEndDate(LocalDate.parse("2013-08-11"), LocalDate.parse("2015-01-21"));

        // Assert the response
        Assertions.assertEquals(2, people.size(), "findBySalary should return 2 people");
    }
}
