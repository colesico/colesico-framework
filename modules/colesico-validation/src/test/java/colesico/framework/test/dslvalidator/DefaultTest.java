package colesico.framework.test.dslvalidator;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.validation.ValidationIssue;
import colesico.framework.validation.Validator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Provider;

public class DefaultTest {

    private Ioc ioc;
    private MyDataBean dataBean;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
        dataBean = new MyDataBean(10L,"AName","AValue");
    }

    @Test
    public void test1() {
        Provider<MyValidatorBuilder> vbProv = ioc.provider(MyValidatorBuilder.class);
        Validator validatorGroup = vbProv.get().buildGroup();
        ValidationIssue vi = validatorGroup.validate(dataBean);
        //System.out.println("Group");
        //System.out.println(vi);

        Validator validatorSubj = vbProv.get().buildSubject();
        ValidationIssue vi2 = validatorSubj.validate(dataBean);
        //System.out.println("Subj");
        //System.out.println(vi2);

        ValidationIssue vi3 = validatorSubj.validate(null);
        //System.out.println("SubjRequired ");
        //System.out.println(vi3);
    }
}
