package colesico.framework.resource.internal;

import colesico.framework.profile.Profile;
import colesico.framework.resource.assist.localization.ObjectiveQualifiers;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.rewriters.localization.L10nRewriterConfigPrototype;

/**
 * Default l10n rewriter config impl
 */
public class L10nRewriterConfigImpl extends L10nRewriterConfigPrototype {

    @Override
    public QualifiersDefinition getQualifiersDefinition() {
        return QualifiersDefinition.LC_QUALIFIERS;
    }

    @Override
    public ObjectiveQualifiers getObjectiveQualifiers(Profile profile) {
        return ObjectiveQualifiers.fromLocaleLC(profile.getLocale());
    }
}
