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
            field("ID", v -> v.getId(), required(), interval(0L, 1L, true)),
            field("NAME", v -> v.getName(), required(), length(1, 2)),
            field("VALUE", v -> v.getValue(), required(), length(1, 5))
        );
    }

    public Validator<MyDataBean> buildSubject() {
        return program(
            "MY_BEAN",
            required(),
            field("ID", v -> v.getId(),
                required(),
                interval(0L, 1L, true),
                interval(-1L, -2L, true)
            ),
            field("NAME", v -> v.getName(),
                group(
                    required(),
                    length(0, 1),
                    length(1, 2)
                )
            ),
            field("VALUE", v -> v.getValue(),
                required(),
                length(1, 5)
            )
        );
    }
}
