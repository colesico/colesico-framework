package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

/**
 * The bin for which the validator builder prototype is generated
 */
public class BeanElement {

    /**
     * Bean origin type
     */
    private final ClassType originType;

    /**
     * Linked validator builders
     */
    private final List<BuilderPrototypeElement> validatorBuilders = new ArrayList<>();

    public BeanElement(ClassType originType) {
        this.originType = originType;
    }

    public void addValidatorBuilder(BuilderPrototypeElement validatorBuilder) {
        validatorBuilders.add(validatorBuilder);
        validatorBuilder.parentBean(this);
    }

    public List<BuilderPrototypeElement> validatorBuilders() {
        return validatorBuilders;
    }

    public ClassType originType() {
        return originType;
    }
}
