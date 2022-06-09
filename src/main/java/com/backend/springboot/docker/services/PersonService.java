package com.backend.springboot.docker.services;

import com.backend.springboot.docker.models.entity.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonService {

    public Iterable<Person> findAll();

    public Optional<Person> findById(int id);

    public Person save(Person entity);

    public void deleteById(int id);

    public List<Person> findBySalary(Double term);

    public Iterable<Person> findAllById(Iterable<Integer> ids);

    public List<Person> findBetweenStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

}
