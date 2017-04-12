import haibo.yan.io.web.service.CustomerService;
import haibo.yan.io.web.service.CustomerServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hyan on 12/25/16.
 */
public class Application {
    public static void main(String[] args) {
        ApplicationContext myAppContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        CustomerService service = myAppContext.getBean("customerService", CustomerService.class);

        System.out.println(service.finaAll().get(0).getFirstName());
    }
}