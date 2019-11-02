package colesico.framework.test.example.config;


import colesico.framework.example.config.MainBean;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ConfigTest {
    private Ioc ioc;
    private MainBean service;
    private Logger logger = LoggerFactory.getLogger(ConfigTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
        service = ioc.instance(MainBean.class);
    }

    @Test
    public void testSimpleConfig(){
        assertEquals(service.getSimpleConfigValue(),"Simple");
    }

    @Test
    public void testSingleConfig(){
        assertEquals(service.getSingleConfigValue(),"Single0;Single1");
    }

    @Test
    public void testPolyconfig(){
        assertTrue("Poly1;Poly2;".equals(service.getPolyconfigValues()) || "Poly2;Poly1;".equals(service.getPolyconfigValues()));
    }

    @Test
    public void testMessageConfig(){
        assertEquals(service.getMessageConfigValues(),"Message1;Message2");
    }

    @Test
    public void testSourceSimple(){
        assertEquals(service.getSourceSimpleConfigValue(),"AppSourceValue");
    }

    @Test
    public void testSourceSingle(){
        assertEquals(service.getSourceSingleConfigValue(),"ConfSourceValue");
    }

}
