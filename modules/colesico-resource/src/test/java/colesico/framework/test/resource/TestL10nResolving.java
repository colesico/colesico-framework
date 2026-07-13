package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestL10nResolving {

    private Ioc ioc;
    private ResourceResolver resourceResolver;

    Logger logger = LoggerFactory.getLogger(TestL10nResolving.class);

    public static final String PATH1 = "root/dir/file.txt";
    public static final String PATH2 = "root/foo/file.txt";
    public static final String PATH3 = "root/folder/file.txt";
    public static final String PATH4 = "root/xxx/file.txt";
    public static final String PATH5 = "root/file.txt";
    public static final String PATH6 = "app/module/dir/file.txt";

    @BeforeClass
    public void init() {
        logger.info("Init L10nResolving test");
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        resourceResolver = ioc.instance(ResourceResolver.class);
    }

    @Test
    public void test1() {
        ioc.instance(TaskScope.class).forTask(() -> {
            String lpath = resourceResolver.resolve(PATH1);
            System.out.println("path=" + lpath);
            assertEquals(lpath, "root/dir/file.txt");

            lpath = resourceResolver.resolve(PATH2);
            System.out.println("path=" + lpath);
            assertEquals(lpath, "root/foo/file_en.txt");

            lpath = resourceResolver.resolve(PATH3);
            System.out.println("path=" + lpath);
            assertEquals(lpath, "root/folder/file_RU.txt");

            lpath = resourceResolver.resolve(PATH4);
            System.out.println("path=" + lpath);
            assertEquals(lpath, "root/xxx/file_en_RU.txt");

            lpath = resourceResolver.resolve(PATH5);
            System.out.println("path=" + lpath);
            assertEquals(lpath, "root_RU/file.txt");

            lpath = resourceResolver.resolve(PATH6);
            System.out.println("path=" + lpath);
            assertEquals(lpath, "app2/module2/dir_en_RU/file_en_RU.txt");
        });
    }
}
