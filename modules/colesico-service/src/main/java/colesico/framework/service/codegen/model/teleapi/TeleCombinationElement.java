package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.service.codegen.model.ServiceParameterElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent combined parameters class and corresponding method parameter
 */
public class TeleCombinationElement extends TeleParameterElement {

    /**
     * Combination class
     */
    private final ClassElement originClass;

    private final List<TeleCombinationFieldElement> fields = new ArrayList<>();

    private final List<TeleCombinationElement> subCombinations = new ArrayList<>();

    public TeleCombinationElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParameter, ClassElement originClass) {
        super(parentTeleCommand, serviceParameter);
        this.originClass = originClass;
    }

    public ClassElement originClass() {
        return originClass;
    }

    public List<TeleCombinationFieldElement> fields() {
        return fields;
    }

    public List<TeleCombinationElement> subCombinations() {
        return subCombinations;
    }

}
