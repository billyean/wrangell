package haibo.yan.io.web.repository;

import haibo.yan.io.web.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
@Repository
public class HibernateCustomerRepositoryImpl implements CustomerRepository {
    @Value("${dbUsername}")
    private String dbUsername;

    @Override
    public List<Customer> findAll(){
        System.out.println("dbUsername : " + dbUsername);
        List<Customer> customers = new ArrayList<>();

        Customer customer = new Customer();
        customer.setFirstName("Haibo");
        customer.setLastName("Yan");
        customers.add(customer);

        return customers;
    }
}
