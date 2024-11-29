package colesico.framework.test.example.jdbirec;

import colesico.framework.example.jdbirec.AppService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class JdbiRecExampleTest {


    private Ioc ioc;
    private Logger logger = LoggerFactory.getLogger(JdbiRecExampleTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
    }

    @Test
    public void testHelloWorld(){
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        //service
    }
}
