package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.resource.rewriters.localization.L10nMode;
import colesico.framework.resource.rewriters.localization.L10nRewriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestL10nRewriter {

    private Ioc ioc;
    private L10nRewriter rewriter;
    Logger logger = LoggerFactory.getLogger(TestL10nRewriter.class);

    public static final String PATH1 = "root/dir/file.txt";
    public static final String PATH2 = "root/foo/file.txt";
    public static final String PATH3 = "root/folder/file.txt";
    public static final String PATH4 = "root/xxx/file.txt";
    public static final String PATH5 = "root/file.txt";

    @BeforeClass
    public void init() {
        logger.info("Init test");
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        rewriter = ioc.instance(L10nRewriter.class);
    }

    @Test
    public void test1() {
        rewriter.l10n(PATH1, L10nMode.FILE, "L=en;C=GB");
        rewriter.l10n(PATH2, L10nMode.FILE, "L=en");
        rewriter.l10n(PATH3, L10nMode.FILE, "C=RU");
        rewriter.l10n(PATH4, L10nMode.FILE, "L=en;C=RU");
        rewriter.l10n(PATH5, L10nMode.DIR, "C=RU");


        String lpath = rewriter.rewrite(PATH1);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/dir/file.txt");

        lpath = rewriter.rewrite(PATH2);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/foo/file_en.txt");

        lpath = rewriter.rewrite(PATH3);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/folder/file_RU.txt");

        lpath = rewriter.rewrite(PATH4);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root/xxx/file_en_RU.txt");

        lpath = rewriter.rewrite(PATH5);
        System.out.println("path=" + lpath);
        assertEquals(lpath, "root_RU/file.txt");

        rewriter.l10n("file.txt", L10nMode.FILE, "C=RU");
        lpath = rewriter.rewrite("file.txt");
        assertEquals(lpath, "file_RU.txt");

        rewriter.l10n("file2.txt", L10nMode.DIR, "C=RU");
        lpath = rewriter.rewrite("file2.txt");
        assertEquals(lpath, "_RU/file2.txt");

        rewriter.l10n(".file3.txt", L10nMode.FILE, "C=RU");
        lpath = rewriter.rewrite(".file3.txt");
        assertEquals(lpath, ".file3_RU.txt");

    }
}
