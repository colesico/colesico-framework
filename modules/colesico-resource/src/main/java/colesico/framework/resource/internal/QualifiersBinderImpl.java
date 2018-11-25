package colesico.framework.resource.internal;

import colesico.framework.resource.ResourceConfig;

final class QualifiersBinderImpl implements ResourceConfig.QualifiersBinder {

    private final LocalizingTool localizationTool;

    public QualifiersBinderImpl(LocalizingTool localizationTool) {
        this.localizationTool = localizationTool;
    }

    @Override
    public ResourceConfig.QualifiersBinder bind(String path, String... qualifiersSetSpec) {
        localizationTool.addQualifiers(path, qualifiersSetSpec);
        return this;
    }
}
