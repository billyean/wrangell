package io.haibo.controller;

import io.haibo.model.Service;
import io.haibo.repositories.ServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ServiceController {
    @Autowired
    ServiceJpaRepository repository;

    @GetMapping(value = "/service/{id}")
    public Service getServiceById(@PathVariable("id") int id) {
        return repository.findById(id);
    }

    @GetMapping(value = "/services")
    public List<Service> getServices() {
        return repository.getAll();
    }
}
