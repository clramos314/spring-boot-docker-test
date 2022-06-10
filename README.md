# SpringBoot - Docker test

## Before anything...
Check if you have installed these features in your machine:

* **JDK 8**,
* **Maven 3.6.x**,
* **Docker client** and **Docker desktop**

## Build 
You can build this project using

```mvn clean install -U -DskipTests```

## Run it!
First, start the docker container with the DB:

```docker-compose up```

**Temporarily:

access by a postgres client and execute manually the sql scripts under /src/main/resources/db/migration:
  
  ```V1__create_person_table.sql```
  ```V2__create_job_table.sql```
  ```V3__add_test_data.sql```

Now, you can build with tests

```mvn clean install -U```

then, you can run the Spring Boot application via:

```mvn spring-boot:run```
