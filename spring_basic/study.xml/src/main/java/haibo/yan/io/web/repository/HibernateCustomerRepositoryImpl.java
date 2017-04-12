package haibo.yan.io.web.repository;

import haibo.yan.io.web.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
public class HibernateCustomerRepositoryImpl implements CustomerRepository {
    private String dbUsername;

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    @Override
    public List<Customer> findAll(){
        System.out.println(dbUsername);
        List<Customer> customers = new ArrayList<>();

        Customer customer = new Customer();
        customer.setFirstName("Haibo");
        customer.setLastName("Yan");
        customers.add(customer);

        return customers;
    }
}
