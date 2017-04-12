package haibo.yan.io.web.service;

import haibo.yan.io.web.model.Customer;
import haibo.yan.io.web.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
@Service("foo")
@Scope(SCOPE_PROTOTYPE)
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

//    public CustomerServiceImpl() {
//
//    }
//
//    public CustomerServiceImpl(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        System.out.println("Using setter injection");
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> finaAll() {
        return customerRepository.findAll();
    }
}
