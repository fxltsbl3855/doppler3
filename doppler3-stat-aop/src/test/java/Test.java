/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */

import com.sinoservices.stat.aop.HelloService;
import com.sinoservices.stat.aop.JobCodeStat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println(JobCodeStat.start("orderId=1"));
//        try {
//            Thread.sleep(500L);
//            if(2 ==1)
//            throw new RuntimeException("dd");
//            //code
//            System.out.println(JobCodeStat.endOk());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(JobCodeStat.endError());
//        }

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        HelloService helloService = (HelloService)context.getBean("helloService");
        System.out.println(helloService.addOrderById("aa",new Date(),3,3.45D));

    }
}
