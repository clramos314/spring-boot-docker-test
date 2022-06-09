package com.backend.springboot.docker.services;

import com.backend.springboot.docker.models.entity.Job;
import com.backend.springboot.docker.models.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    protected JobRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Job> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Job> findById(int id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Job save(Job entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Job> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

}

