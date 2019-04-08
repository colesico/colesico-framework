package colesico.framework.test.dslvalidator;

import colesico.framework.dslvalidator.builder.ValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.ioc.Unscoped;
import colesico.framework.validation.Validator;

import javax.inject.Inject;

@Unscoped
public class MyValidatorBuilder extends ValidatorBuilder {

    @Inject
    public MyValidatorBuilder(ValidatorMessages msg) {
        super(msg);
    }

    public Validator<MyDataBean> buildGroup() {
        return program(
                with(MyDataBean.class)
                        .on("ID", v -> v.getId(),
                                required(),
                                interval(0L, 1L, true)
                        )
                        .on("NAME", v -> v.getName(),
                                required(),
                                length(1, 2)
                        )
                        .on("VALUE", v -> v.getValue(),
                                required(),
                                length(1, 5)
                        ).end()
        );
    }

    public Validator<MyDataBean> buildSubject() {
        return program(
                "MY_BEAN",
                required(),
                with(MyDataBean.class)
                        .on("ID", v -> v.getId(),
                                required(),
                                interval(0L, 1L, true),
                                interval(-1L, -2L, true)
                        )
                        .on("NAME", v -> v.getName(),
                                group(
                                        required(),
                                        length(0, 1),
                                        length(1, 2)
                                )
                        )
                        .on("VALUE", v -> v.getValue(),
                                required(),
                                length(1, 5)
                        ).end()
        );
    }
}
