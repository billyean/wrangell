package io.haibo.controller;

import io.haibo.model.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class ServiceController {
    @GetMapping(value = "/service/{id}")
    public Service getServiceById(@PathVariable("id") int id) {
        return new Service(1, "Test", "Test", Arrays.asList(30, 60), 2.0, 1);
    }
}
