import haibo.yan.io.web.service.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Haibo Yan
 * @since 12/25/16.
 */
public class Application {
    public static void main(String[] args) {
        ApplicationContext myAppContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        CustomerService service = myAppContext.getBean("customerService", CustomerService.class);
        CustomerService service1 = myAppContext.getBean("customerService", CustomerService.class);
        System.out.println(service == service1);

        System.out.println(service.finaAll().get(0).getFirstName());
    }
}
