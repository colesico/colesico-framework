package colesico.framework.test.example.slf4j;

import colesico.framework.example.slf4j.MainBean;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertTrue;

public class Slf4jTest {
    private Ioc ioc;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
    }

    @Test
    public void testLog() throws Exception{
        MainBean mainBean = ioc.instance(MainBean.class);
        mainBean.logMessage("TestLogMessage");

        String logText = new String(Files.readAllBytes(Paths.get("target/test.log")));
        assertTrue(StringUtils.contains(logText,"TestLogMessage"));
        assertTrue(StringUtils.contains(logText,MainBean.class.getName()));
    }

}
