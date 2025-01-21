package colesico.framework.resource.rewriters.localization;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.profile.Profile;
import colesico.framework.resource.assist.localization.ObjectiveQualifiers;
import colesico.framework.resource.assist.localization.QualifiersDefinition;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class L10nRewriterCoreConfigPrototype {

    /**
     * Setup {@link QualifiersDefinition} fo L10n rewriter
     */
    abstract public QualifiersDefinition getQualifiersDefinition();

    /**
     * Returns {@link ObjectiveQualifiers} from on given profile
     */
    abstract public ObjectiveQualifiers getObjectiveQualifiers(Profile profile);

}
