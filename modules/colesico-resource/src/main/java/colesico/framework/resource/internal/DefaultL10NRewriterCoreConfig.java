package colesico.framework.resource.internal;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.profile.Profile;
import colesico.framework.resource.assist.localization.ObjectiveQualifiers;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.rewriters.localization.L10nRewriterCoreConfigPrototype;

/**
 * Default l10n rewriter config impl
 */
@Config
@Substitute(Substitution.STUB)
public class DefaultL10NRewriterCoreConfig extends L10nRewriterCoreConfigPrototype {

    @Override
    public QualifiersDefinition getQualifiersDefinition() {
        return QualifiersDefinition.ofLC();
    }

    @Override
    public ObjectiveQualifiers getObjectiveQualifiers(Profile profile) {
        return ObjectiveQualifiers.ofLocaleLC(profile.getLocale());
    }
}
