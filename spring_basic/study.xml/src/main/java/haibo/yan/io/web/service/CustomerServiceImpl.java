package haibo.yan.io.web.service;

import haibo.yan.io.web.model.Customer;
import haibo.yan.io.web.repository.CustomerRepository;

import java.util.List;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
public class CustomerServiceImpl implements CustomerService {
    /**
     * Customer repository
     */
    private CustomerRepository customerRepository;

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerServiceImpl(){}

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> finaAll() {
        return customerRepository.findAll();
    }
}
