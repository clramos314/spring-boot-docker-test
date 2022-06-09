package com.backend.springboot.docker.services;

import com.backend.springboot.docker.models.entity.Person;
import com.backend.springboot.docker.models.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    protected PersonRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Person> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Person save(Person entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findBySalary(Double term) {
        return repository.findBySalary(term);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Person> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findBetweenStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        return repository.findBetweenStartDateAndEndDate(startDate, endDate);
    }

}
