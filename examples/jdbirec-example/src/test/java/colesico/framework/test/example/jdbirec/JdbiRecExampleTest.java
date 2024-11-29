package colesico.framework.test.example.jdbirec;

import colesico.framework.example.jdbirec.AppService;
import colesico.framework.example.jdbirec.view.User;
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
    public void testDefaultView() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        User user = service.getUser();
        assertEquals(user.getName(), "Ivan");
        assertEquals(user.getExtra().getAddress(), "extra-address");
    }

    @Test
    public void testFullView() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        User user = service.getUserFull();
        assertEquals(user.getName(), null);
        assertEquals(user.getExtra().getAddress(), "extra-address");
    }

    @Test
    public void testBriefView() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        User user = service.getUserBrief();
        assertEquals(user.getId(), 1);
        assertEquals(user.getExtra().getAddress(), null);
    }
}
