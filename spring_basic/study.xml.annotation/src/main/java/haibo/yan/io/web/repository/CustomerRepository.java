package haibo.yan.io.web.repository;

import haibo.yan.io.web.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hyan on 12/25/16.
 */
public interface CustomerRepository {
    List<Customer> findAll();
}
