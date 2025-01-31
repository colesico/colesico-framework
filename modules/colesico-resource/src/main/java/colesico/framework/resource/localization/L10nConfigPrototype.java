package colesico.framework.resource.localization;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.profile.Profile;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class L10nConfigPrototype {

    /**
     * Setup {@link QualifiersDefinition} fo L10n rewriter
     */
    abstract public QualifiersDefinition getQualifiersDefinition();

    /**
     * Returns {@link ObjectiveQualifiers} from on given profile
     */
    abstract public ObjectiveQualifiers getObjectiveQualifiers(Profile profile);


}
