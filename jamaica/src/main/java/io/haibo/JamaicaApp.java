package io.haibo;

import io.haibo.model.Service;
import io.haibo.repositories.ServiceJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class JamaicaApp implements CommandLineRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    ServiceJpaRepository repository;
//    ServiceJdbcDao jdbcDao;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(JamaicaApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        for (Service service :  jdbcDao.findAll()){
//            logger.info("service : {} ", service);
//        }

//        logger.info("service : {} ",  jdbcDao.findById(2));
//        logger.info("delete {} row ",  jdbcDao.deleteById(2));
        LOGGER.info("service : {} ", repository.findById(1));

        Service newService = new Service("Test1", "Test2", 0, 90, 2.0, 1);
        Service created = repository.create(newService);
        LOGGER.info("service : {} ", repository.findById(newService.getId()));
        repository.delete(created.getId());

        LOGGER.info("service : {} ", repository.findByName("Facial services"));
    }
}
