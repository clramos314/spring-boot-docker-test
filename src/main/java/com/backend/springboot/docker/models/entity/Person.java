package com.backend.springboot.docker.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String name;

    @Column(name = "ENTRY_DATE", columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate entryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="job_id", nullable = false)
    @JsonIgnoreProperties(value= {"hibernateLazyInitializer", "handler", "people"}, allowSetters = true)
    private Job job;


    public Person(){}

    public Person(int id, String name, LocalDate entryDate){
        this.id = id;
        this.name = name;
        this.entryDate = entryDate;
    }

    public Person(int id, String name, String entryDate, Job job){
        this.id = id;
        this.name = name;
        this.entryDate = LocalDate.parse(entryDate);
        this.job = job;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public Job getJob() { return job; }

    public void setJob(Job job) { this.job = job; }

    @Override
    public boolean equals(Object obj) {

        if(this == obj) {
            return true;
        }

        if(!(obj instanceof Person)) {
            return false;
        }

        Person a = (Person) obj;

        return this.id == a.getId();
    }

}

