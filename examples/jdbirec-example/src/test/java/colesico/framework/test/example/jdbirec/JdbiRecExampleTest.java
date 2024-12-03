package colesico.framework.test.example.jdbirec;

import colesico.framework.example.jdbirec.AppService;
import colesico.framework.example.jdbirec.selectas.SAUser;
import colesico.framework.example.jdbirec.view.VWUser;
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
    public void testSelectAs() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        SAUser user = service.getSAUser();
        assertEquals(user.getName(), "USER:Ivan");
    }

    @Test
    public void testDefaultView() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        VWUser user = service.getVWUser();
        assertEquals(user.getName(), "Ivan");
        assertEquals(user.getExtra().getAddress(), "extra-address");
    }

    @Test
    public void testFullView() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        VWUser user = service.getVWUserFull();
        assertEquals(user.getName(), null);
        assertEquals(user.getExtra().getAddress(), "extra-address");
    }

    @Test
    public void testBriefView() {
        logger.info("Run JDBI test");
        AppService service = ioc.instance(AppService.class);
        VWUser user = service.getVWUserBrief();
        assertEquals(user.getId(), 1);
        assertEquals(user.getExtra().getAddress(), null);
    }
}
