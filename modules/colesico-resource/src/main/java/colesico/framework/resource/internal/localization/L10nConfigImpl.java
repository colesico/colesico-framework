package colesico.framework.resource.internal.localization;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.profile.Profile;
import colesico.framework.resource.localization.ObjectiveQualifiers;
import colesico.framework.resource.localization.Qualifier;
import colesico.framework.resource.localization.QualifiersDefinition;
import colesico.framework.resource.localization.L10nConfigPrototype;

import java.util.Locale;

/**
 * Default l10n rewriter core config impl
 */
@Config
@Substitute(Substitution.STUB)
public class L10nConfigImpl extends L10nConfigPrototype {

    private static final QualifiersDefinition QUALIFIERS_DEFINITION = QualifiersDefinition.of(Qualifier.LANGUAGE_QUALIFIER, Qualifier.COUNTRY_QUALIFIER);

    @Override
    public QualifiersDefinition getQualifiersDefinition() {
        return QUALIFIERS_DEFINITION;
    }

    @Override
    public ObjectiveQualifiers getObjectiveQualifiers(Profile profile) {
        Locale locale = profile.getLocale();
        return ObjectiveQualifiers.of(
                QUALIFIERS_DEFINITION,
                Qualifier.language(locale.getLanguage()),
                Qualifier.country(locale.getCountry())
        );
    }

}
