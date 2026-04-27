package colesico.framework.resource.internal.l10n;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.profile.Profile;
import colesico.framework.resource.l10n.ObjectiveQualifiers;
import colesico.framework.resource.l10n.Qualifier;
import colesico.framework.resource.l10n.QualifiersDefinition;
import colesico.framework.resource.l10n.L10nConfigPrototype;

import java.util.Locale;

/**
 * Default l10n rewriter core config impl
 */
@Config
@Substitute(Substitution.STUB)
public class L10nConfigImpl extends L10nConfigPrototype {

    private static final QualifiersDefinition QUALIFIERS_DEFINITION = QualifiersDefinition.of(Qualifier.LANGUAGE_QUALIFIER, Qualifier.COUNTRY_QUALIFIER);

    @Override
    public QualifiersDefinition qualifiersDefinition() {
        return QUALIFIERS_DEFINITION;
    }

    @Override
    public ObjectiveQualifiers objectiveQualifiers(Profile profile) {
        Locale locale = profile.locale();
        return ObjectiveQualifiers.of(
                QUALIFIERS_DEFINITION,
                Qualifier.language(locale.getLanguage()),
                Qualifier.country(locale.getCountry())
        );
    }

}
