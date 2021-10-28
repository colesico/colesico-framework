package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.ClassType;

public class ConfigScopedElement {

    /**
     * Scoped annotation type
     */
    private ClassType scopedAnnotation;

    private boolean specifiedExplicitly;

    public ConfigScopedElement(ClassType scopedAnn, boolean specifiedExplicitly) {
        this.scopedAnnotation = scopedAnn;
        this.specifiedExplicitly = specifiedExplicitly;
    }

    public ClassType getScopedAnnotation() {
        return scopedAnnotation;
    }

    public void setScopedAnnotation(ClassType scopedAnnotation) {
        this.scopedAnnotation = scopedAnnotation;
    }

    public boolean isSpecifiedExplicitly() {
        return specifiedExplicitly;
    }

    public void setSpecifiedExplicitly(boolean specifiedExplicitly) {
        this.specifiedExplicitly = specifiedExplicitly;
    }
}
