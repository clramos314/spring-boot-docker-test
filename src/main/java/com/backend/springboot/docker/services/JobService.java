package com.backend.springboot.docker.services;

import com.backend.springboot.docker.models.entity.Job;

import java.util.Optional;

public interface JobService {

    public Iterable<Job> findAll();

    public Optional<Job> findById(int id);

    public Job save(Job entity);

    public void deleteById(int id);

    public Iterable<Job> findAllById(Iterable<Integer> ids);

}
