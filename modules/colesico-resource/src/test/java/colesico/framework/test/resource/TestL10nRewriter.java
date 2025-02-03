package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.resource.ResourceKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestL10nRewriter {

    private Ioc ioc;
    private ResourceKit resourceKit;

    Logger logger = LoggerFactory.getLogger(TestL10nRewriter.class);

    public static final String PATH1 = "root/dir/file.txt";
    public static final String PATH2 = "root/foo/file.txt";
    public static final String PATH3 = "root/folder/file.txt";
    public static final String PATH4 = "root/xxx/file.txt";
    public static final String PATH5 = "root/file.txt";
    public static final String PATH6 = "app/module/dir/file.txt";

    @BeforeClass
    public void init() {
        logger.info("Init L10nRewriter test");
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        resourceKit = ioc.instance(ResourceKit.class);
    }

    @Test
    public void test1() {


        String lpath = resourceKit.localizePath(PATH1);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/dir/file.txt");

        lpath = resourceKit.localizePath(PATH2);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/foo/file_en.txt");

        lpath = resourceKit.localizePath(PATH3);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/folder/file_RU.txt");

        lpath = resourceKit.localizePath(PATH4);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/xxx/file_en_RU.txt");

        lpath = resourceKit.localizePath(PATH5);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root_RU/file.txt");

        lpath = resourceKit.localizePath(PATH6);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "app2/module2/dir_en_RU/file_en_RU.txt");
    }
}
