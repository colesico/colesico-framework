package colesico.framework.test.example.jdbirec;

import colesico.framework.example.jdbirec.AppService;
import colesico.framework.example.jdbirec.join.JUser;
import colesico.framework.example.jdbirec.renaming.RUser;
import colesico.framework.example.jdbirec.selectas.SUser;
import colesico.framework.example.jdbirec.view.VUser;
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
    public void testJoin() {
        logger.info("Test join");
        AppService service = ioc.instance(AppService.class);
        JUser user = service.getJUser();
        //assertEquals(user.getHome().getAddress(), "Moscow 1");
    }

    @Test
    public void testRenaming() {
        logger.info("Test renaming");
        AppService service = ioc.instance(AppService.class);
        RUser user = service.getRUser();
        assertEquals(user.getHome().getAddress(), "Moscow 1");
    }

    @Test
    public void testSelectAs() {
        logger.info("Test select as");
        AppService service = ioc.instance(AppService.class);
        SUser user = service.getSUser();
        assertEquals(user.getName(), "USER:Ivan");
        assertEquals(user.getCount(), 1);
    }

    @Test
    public void testDefaultView() {
        logger.info("Test default view");
        AppService service = ioc.instance(AppService.class);
        VUser user = service.getVUser();
        assertEquals(user.getName(), "Ivan");
        assertEquals(user.getExtra().getAddress(), "extra-address");
    }

    @Test
    public void testFullView() {
        logger.info("Test full view");
        AppService service = ioc.instance(AppService.class);
        VUser user = service.getVUserFull();
        assertEquals(user.getName(), null);
        assertEquals(user.getExtra().getAddress(), "extra-address");
    }

    @Test
    public void testBriefView() {
        logger.info("Test brief view");
        AppService service = ioc.instance(AppService.class);
        VUser user = service.getVUserBrief();
        assertEquals(user.getId(), 1);
        assertEquals(user.getExtra().getAddress(), null);
    }
}
