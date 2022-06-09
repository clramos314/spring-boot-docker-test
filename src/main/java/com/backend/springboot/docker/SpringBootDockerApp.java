package com.backend.springboot.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan({"com.backend.springboot.docker.models.entity"})
public class SpringBootDockerApp {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootDockerApp.class, args);
  }
}
