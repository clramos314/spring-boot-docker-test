package com.backend.springboot.docker.controllers;

import com.backend.springboot.docker.models.entity.Person;
import com.backend.springboot.docker.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    protected PersonService service;

    @GetMapping
    public ResponseEntity<?> doList() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> doSee(@PathVariable int id) {
        Optional<Person> o = service.findById(id);
        if (!o.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(o.get());
    }

    @PostMapping
    public ResponseEntity<?> doCreate(@Valid @RequestBody Person person, BindingResult result) {

        if (result.hasErrors()) {
            return this.validate(result);
        }
        Person personDb = service.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personDb);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> doDelete(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validate(BindingResult result) {
        Map<String, Object> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), " The field " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> doUpdate(@Valid @RequestBody Person person, BindingResult result, @PathVariable int id) {

        if (result.hasErrors()) {
            return this.validate(result);
        }

        Optional<Person> o = service.findById(id);

        if (!o.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Person personDb = o.get();
        personDb.setName(person.getName());
        personDb.setEntryDate(person.getEntryDate());
        personDb.setJob(person.getJob());

        return ResponseEntity.status(HttpStatus.OK).body(service.save(personDb));
    }

    @GetMapping("/filter-by-salary/{limit}")
    public ResponseEntity<?> doFilterBySalary(@PathVariable Double limit) {
        List<Person> people = service.findBySalary(limit);
        return ResponseEntity.ok().body(people);
    }

    @GetMapping("/filter-between-dates/{start_date}/{end_date}")
    public ResponseEntity<?> doFindBetweenStartDateAndEndDate(@PathVariable(value = "start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @PathVariable(value = "end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Person> people = service.findBetweenStartDateAndEndDate(startDate, endDate);
        return ResponseEntity.ok().body(people);
    }

}
