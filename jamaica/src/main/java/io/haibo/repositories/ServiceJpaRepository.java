package io.haibo.repositories;

import io.haibo.model.Service;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ServiceJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Service findById(Integer id) {
        return em.find(Service.class, id);
    }
}

