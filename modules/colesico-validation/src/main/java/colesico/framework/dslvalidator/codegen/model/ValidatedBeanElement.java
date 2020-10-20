package colesico.framework.dslvalidator.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;

import java.util.ArrayList;
import java.util.List;

public class ValidatedBeanElement {

    /**
     * Bean origin class element
     */
    private final ClassElement originClass;

    private final List<ValidatorBuilderPrototypeElement> builderPrototypes = new ArrayList<>();

    public ValidatedBeanElement(ClassElement originClass) {
        this.originClass = originClass;
    }

    public void addBuilderPrototype(ValidatorBuilderPrototypeElement builderPrototype) {
        builderPrototypes.add(builderPrototype);
        builderPrototype.setParentVB(this);
    }

    public List<ValidatorBuilderPrototypeElement> getBuilderPrototypes() {
        return builderPrototypes;
    }

    public ClassElement getOriginClass() {
        return originClass;
    }
}
