package com.backend.springboot.docker.models.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.backend.springboot.docker.models.entity.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select p from Person p join fetch p.job j where j.salary <= ?1")
    public List<Person> findBySalary(Double term);

    @Query("select p from Person p join fetch p.job j where p.entryDate between ?1 and ?2")
    public List<Person> findBetweenStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

}
