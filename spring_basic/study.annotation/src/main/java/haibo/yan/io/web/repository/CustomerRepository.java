package haibo.yan.io.web.repository;

import haibo.yan.io.web.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
@Repository
public interface CustomerRepository {
    List<Customer> findAll();
}
