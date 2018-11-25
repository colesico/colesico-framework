package colesico.framework.resource.internal;

import colesico.framework.resource.ResourceConfig;

public class PropertiesBinderImpl implements ResourceConfig.PropertiesBinder {

    private final EvaluationTool evaluationTool;

    public PropertiesBinderImpl(EvaluationTool evaluationTool) {
        this.evaluationTool = evaluationTool;
    }

    @Override
    public ResourceConfig.PropertiesBinder bind(String name, String value) {
        evaluationTool.addProperty(name, value);
        return this;
    }

}
