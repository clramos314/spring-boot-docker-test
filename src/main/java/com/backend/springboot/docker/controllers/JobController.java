package com.backend.springboot.docker.controllers;

import com.backend.springboot.docker.models.entity.Job;
import com.backend.springboot.docker.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    protected JobService service;

    @GetMapping
    public ResponseEntity<?> doList(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> doSee(@PathVariable int id){
        Optional<Job> o = service.findById(id);
        if(!o.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(o.get());
    }

    @PostMapping
    public ResponseEntity<?> doCreate(@Valid @RequestBody Job job, BindingResult result){

        if(result.hasErrors()) {
            return this.validar(result);
        }
        Job jobDb = service.save(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobDb);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> doDelete(@PathVariable int id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validar(BindingResult result){
        Map<String, Object> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), " The field " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> doUpdate(@Valid @RequestBody Job job, BindingResult result, @PathVariable int id){

        if(result.hasErrors()) {
            return this.validar(result);
        }

        Optional<Job> o = service.findById(id);

        if(o.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Job jobDb = o.get();
        jobDb.setJobName(job.getJobName());
        jobDb.setJobDescription(job.getJobDescription());
        jobDb.setSalary(job.getSalary());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(jobDb));
    }

}

