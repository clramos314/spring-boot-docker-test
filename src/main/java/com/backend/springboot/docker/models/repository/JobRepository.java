package com.backend.springboot.docker.models.repository;

import com.backend.springboot.docker.models.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

}
