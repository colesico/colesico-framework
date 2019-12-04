package colesico.framework.test.example.jdbi;

import colesico.framework.example.jdbi.AppService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JdbiTest {
    private Ioc ioc;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
    }

    @Test
    public void testHelloWorld(){
        AppService service = ioc.instance(AppService.class);
        assertEquals(service.readValue(1),"a-value");
    }
}
