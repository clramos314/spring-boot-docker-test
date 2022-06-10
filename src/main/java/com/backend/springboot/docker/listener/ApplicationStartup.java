package com.backend.springboot.docker.listener;

import com.backend.springboot.docker.models.entity.Job;
import com.backend.springboot.docker.models.entity.Person;
import com.backend.springboot.docker.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    protected PersonService service;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        try {
            Iterable<Person> people = service.findAll();

            log.info("---> Person and Job salary grouping by Job name:");

            Map<Job, List<Person>> groupByJobNameMap =
                    StreamSupport.stream(people.spliterator(), false).
                            collect(Collectors.groupingBy(Person::getJob));

            groupByJobNameMap.forEach((k, v) -> System.out.println("Job name : " + k.getJobName() + ", Job salary : " + k.getSalary() + ", People : " + v.stream().map(Person::getName).collect(Collectors.toList())));
        }
        catch (Exception e) {
            log.warn("---> DB is not yet populated:");
        }

        return;
    }
}
