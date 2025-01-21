package colesico.framework.resource.internal;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.profile.Profile;
import colesico.framework.resource.assist.localization.ObjectiveQualifiers;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.rewriters.localization.L10nRewriterCoreConfigPrototype;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Default l10n rewriter config impl
 */
@Config
@Substitute(Substitution.STUB)
public class DefaultL10NRewriterCoreConfig extends L10nRewriterCoreConfigPrototype {

    @Override
    public QualifiersDefinition getQualifiersDefinition() {
        return QualifiersDefinition.QUALIFIERS_LC;
    }

    @Override
    public ObjectiveQualifiers getObjectiveQualifiers(Profile profile) {
        Locale locale = profile.getLocale();
        return ObjectiveQualifiers.of(
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
                StringUtils.isBlank(locale.getCountry()) ? null : locale.getCountry()
        );
    }
}
