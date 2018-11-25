package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.internal.LocalizingTool;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestLocalizationTool {

    private Ioc ioc;
    private LocalizingTool localizingTool;

    public static final String PATH1 = "root/dir/file.txt";
    public static final String PATH2 = "root/foo/file.txt";
    public static final String PATH3 = "root/folder/file.txt";


    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
        localizingTool = ioc.instance(LocalizingTool.class);
    }

    @Test
    public void test1() {
        localizingTool.addQualifiers(PATH1, "L=en;C=GB");
        localizingTool.addQualifiers(PATH2, "L=en");
        localizingTool.addQualifiers(PATH3, "C=RU");

        String lpath = localizingTool.localize(PATH1, ResourceKit.L10NMode.FILE);
        System.out.println("path=" + lpath);
        lpath = localizingTool.localize(PATH2, ResourceKit.L10NMode.FILE);
        System.out.println("path=" + lpath);
        lpath = localizingTool.localize(PATH3, ResourceKit.L10NMode.FILE);
        System.out.println("path=" + lpath);
    }
}
