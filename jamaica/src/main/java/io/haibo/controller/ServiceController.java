package io.haibo.controller;

import io.haibo.model.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ServiceController {
    @GetMapping(value = "/service/{id}")
    public Service getServiceById(@PathVariable("id") int id) {
        return new Service(1, "Test", "Test", "30, 60", 2.0, 1);
    }

    @GetMapping(value = "/services")
    public List<Service> getServices() {
        return Arrays.asList(
                new Service(1, "Test", "Test", "30, 60", 2.0, 1)
        );
    }
}
