package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

/**
 * The bin for which the validator builder prototype is generated
 */
public class ValidatedBeanElement {

    /**
     * Bean origin type
     */
    private final ClassType originType;

    /**
     * Linked validator builders
     */
    private final List<ValidatorBuilderElement> builders = new ArrayList<>();

    public ValidatedBeanElement(ClassType originType) {
        this.originType = originType;
    }

    public void addBuilder(ValidatorBuilderElement builderElm) {
        builders.add(builderElm);
        builderElm.setParentBean(this);
    }

    public List<ValidatorBuilderElement> getBuilders() {
        return builders;
    }

    public ClassType getOriginType() {
        return originType;
    }
}
