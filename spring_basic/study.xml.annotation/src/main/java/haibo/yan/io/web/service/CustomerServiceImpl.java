package haibo.yan.io.web.service;

import haibo.yan.io.web.model.Customer;
import haibo.yan.io.web.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
//    @Autowired
//    public void setCustomerRepository(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }

    public CustomerServiceImpl(){}

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository){
        System.out.println("Using constructor injection");
        this.customerRepository = customerRepository;
    }

//    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public List<Customer> finaAll() {
        return customerRepository.findAll();
    }
}
