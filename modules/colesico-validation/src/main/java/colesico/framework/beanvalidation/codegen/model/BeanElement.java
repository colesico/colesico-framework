package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

/**
 * The bin for which the validator builder is generated
 */
public class BeanElement {

    /**
     * Bean origin type
     */
    private final ClassType originType;

    /**
     * Linked validator builders
     */
    private final List<ValidatorBuilderElement> validatorBuilders = new ArrayList<>();

    private ValidatorBuilderElement defaultValidatorBuilder = null;

    public BeanElement(ClassType originType) {
        this.originType = originType;
    }

    public void addValidatorBuilder(ValidatorBuilderElement validatorBuilder) {
        validatorBuilders.add(validatorBuilder);
        validatorBuilder.parentBean(this);
    }

    public void setDefaultValidatorBuilder(ValidatorBuilderElement defaultValidatorBuilder) {
        if (this.defaultValidatorBuilder != null) {
            throw CodegenException.of()
                    .message("Default validator builder already defined")
                    .element(originType.unwrap().asElement())
                    .build();
        }
        this.defaultValidatorBuilder = defaultValidatorBuilder;
    }

    public List<ValidatorBuilderElement> validatorBuilders() {
        return validatorBuilders;
    }

    public ClassType originType() {
        return originType;
    }

    public ValidatorBuilderElement defaultValidatorBuilder() {
        return defaultValidatorBuilder;
    }
}
