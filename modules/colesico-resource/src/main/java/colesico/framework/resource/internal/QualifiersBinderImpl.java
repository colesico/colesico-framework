package colesico.framework.resource.internal;

import colesico.framework.resource.ResourceOptionsPrototype;

final class QualifiersBinderImpl implements ResourceOptionsPrototype.QualifiersBinder {

    private final LocalizingTool localizationTool;

    public QualifiersBinderImpl(LocalizingTool localizationTool) {
        this.localizationTool = localizationTool;
    }

    @Override
    public ResourceOptionsPrototype.QualifiersBinder bind(String path, String... qualifiersSetSpec) {
        localizationTool.addQualifiers(path, qualifiersSetSpec);
        return this;
    }
}
