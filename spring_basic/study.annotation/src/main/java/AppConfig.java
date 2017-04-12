
import haibo.yan.io.web.repository.CustomerRepository;
import haibo.yan.io.web.repository.HibernateCustomerRepositoryImpl;
import haibo.yan.io.web.service.CustomerService;
import haibo.yan.io.web.service.CustomerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
@Configuration
@ComponentScan("haibo.yan.io.web")
@PropertySource("app.properties")
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
//    @Bean(name="customerService")
//    public CustomerService getCustomerService() {
//        CustomerServiceImpl service = new CustomerServiceImpl();
//        //service.setCustomerRepository(getCustomerRepository());
//        return service;
//    }
//
//    @Bean(name="customerRepository")
//    public CustomerRepository getCustomerRepository() {
//        return new HibernateCustomerRepositoryImpl();
//    }
}
