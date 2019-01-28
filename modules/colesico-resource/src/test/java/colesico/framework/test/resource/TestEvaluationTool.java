package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.resource.internal.EvaluationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TestEvaluationTool {

    private Ioc ioc;
    private EvaluationTool evaluationTool;

    Logger logger = LoggerFactory.getLogger(TestEvaluationTool.class);


    @BeforeClass
    public void init() {
        logger.info("Init test");
        ioc = IocBuilder.forTests();
        evaluationTool = ioc.instance(EvaluationTool.class);
        evaluationTool.addProperty("$alias", "foo/dummy");
    }

    @Test
    public void test1() {
        String path;

        path = evaluationTool.evaluate("/");
        assertEquals(path, "/");

        path = evaluationTool.evaluate("/home");
        assertEquals(path, "/home");

        path = evaluationTool.evaluate("home");
        assertEquals(path, "home");

        path = evaluationTool.evaluate("$alias/home");
        assertEquals(path, "foo/dummy/home");

        path = evaluationTool.evaluate("/$alias/home");
        assertEquals(path, "/foo/dummy/home");

        path = evaluationTool.evaluate("/$alias/home/");
        assertEquals(path, "/foo/dummy/home/");

        System.out.println(path);


        path = evaluationTool.evaluate("/$alias");
        assertEquals(path, "/foo/dummy");

        path = evaluationTool.evaluate("/$alias/");
        assertEquals(path, "/foo/dummy/");

        path = evaluationTool.evaluate("$alias");
        assertEquals(path, "foo/dummy");
    }
}
