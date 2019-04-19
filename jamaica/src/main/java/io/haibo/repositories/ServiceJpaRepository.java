package io.haibo.repositories;

import io.haibo.model.Service;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class ServiceJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Service findById(Integer id) {
        return em.find(Service.class, id);
    }

    @Transactional
    public Service create(Service service) {
        return em.merge(service);
    }

    @Transactional
    public void delete(int serviceId) {
        Service service = findById(serviceId);
        em.remove(service);
    }
}

