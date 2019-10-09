package colesico.framework.resource.internal;

import colesico.framework.resource.ResourceOptionsPrototype;

public class PropertiesBinderImpl implements ResourceOptionsPrototype.PropertiesBinder {

    private final EvaluationTool evaluationTool;

    public PropertiesBinderImpl(EvaluationTool evaluationTool) {
        this.evaluationTool = evaluationTool;
    }

    @Override
    public ResourceOptionsPrototype.PropertiesBinder bind(String name, String value) {
        evaluationTool.addProperty(name, value);
        return this;
    }

}
