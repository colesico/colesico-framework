package colesico.framework.resource.internal.l10n;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.Profile;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.assist.PathTrie;
import colesico.framework.resource.l10n.*;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

@Singleton
public class Localizer {

    private static final Logger log = LoggerFactory.getLogger(Localizer.class);

    private final PathTrie<NameConfig> pathTrie = PathTrie.of();

    private final L10nConfigPrototype config;
    private final Provider<Profile> profileProv;

    @Inject
    public Localizer(L10nConfigPrototype config,
                     Polysupplier<L10nOptionsPrototype> options,
                     Provider<Profile> profileProv) {
        this.config = config;
        this.profileProv = profileProv;

        var opt = new L10nOptionsPrototype.Options();
        options.forEach(o -> o.configure(opt));

        QualifiersDefinition definition = config.getQualifiersDefinition();
        for (var pc : opt.baseNameOptions()) {
            addLocalization(pc.name(), pc.qualifiers(definition));
        }
    }

    /**
     * Register resource name localization for specific resource.
     * The following placeholders can be used in the resource name template:
     * -   {Q}  qualifiers suffix placeholder i.e. messages{Q}.txt  messages_ru_RU.txt
     * -   {{str=substitution}} name part for substitution :  /app/{module>ext}/messages.txt -> /app/ext/messages.txt
     *
     * @param nameTemplate      - resource name template
     * @param subjectQualifiers - subject qualifiers values
     */
    private void addLocalization(String nameTemplate,
                                 SubjectQualifiers[] subjectQualifiers) {

        TemplateParser templateParser = TemplateParser.parse(nameTemplate);
        String resourceName = templateParser.getResourceName();

        final PathTrie.Node<NameConfig> node = pathTrie.add(resourceName);
        NameConfig nameConfig = node.getValue();
        QualifiersMatcher<QualifierConfig> matcher;
        if (nameConfig == null) {
            matcher = new QualifiersMatcher<>();
            nameConfig = new NameConfig(matcher);
            node.setValue(nameConfig);
        } else {
            matcher = nameConfig.matcher();
        }

        Tag[] tags = templateParser.getTags();
        for (SubjectQualifiers sq : subjectQualifiers) {
            var prvRewriting = matcher.addQualifiers(sq, new QualifierConfig(tags));
            if (prvRewriting != null) {
                throw new ResourceException("Resource name localization already defined. Name=" + resourceName + "; Qualifiers=" + sq);
            }
        }

    }

    /**
     * Returns best suitable to current profile localized resource name
     *
     * @param baseName resource base name
     */
    public String localize(String baseName) {

        NameConfig rewriting = pathTrie.find(baseName);

        if (rewriting == null) {
            return baseName;
        }

        var profile = profileProv.get();
        var matchResult = rewriting.matcher().match(config.getObjectiveQualifiers(profile));

        if (matchResult == null) {
            return baseName;
        }

        return rewriteByTags(baseName, matchResult);
    }

    /**
     * Returns  localized resource names suitable for given base name.
     * The first name corresponds to the result of the method {@link #localize(String)} - the most suitable localization
     * The last one name - default localization  (less suitable)
     *
     * @param baseName resource base name
     */
    public String[] localizations(String baseName) {
        NameConfig rewriting = pathTrie.find(baseName);

        if (rewriting == null) {
            return new String[]{baseName};
        }

        Set<String> result = new LinkedHashSet<>();
        var profile = profileProv.get();
        ObjectiveQualifiers objectiveQualifiers = config.getObjectiveQualifiers(profile);

        int n = objectiveQualifiers.size();
        for (int i = 0; i < n; i++) {
            var matchResult = rewriting.matcher().match(objectiveQualifiers);
            if (matchResult == null) {
                continue;
            }
            String rewritedName = rewriteByTags(baseName, matchResult);
            result.add(rewritedName);

            // Reduce qualifiers significant capacity
            String[] values = objectiveQualifiers.getValues();
            values[n - i - 1] = null;
            objectiveQualifiers = ObjectiveQualifiers.of(values);
        }

        if (result.isEmpty()) {
            return new String[]{baseName};
        }

        return result.toArray(String[]::new);
    }

    public ObjectiveQualifiers getObjectiveQualifiers() {
        return config.getObjectiveQualifiers(profileProv.get());
    }

    public ObjectiveQualifiers getObjectiveQualifiers(Profile profile) {
        return config.getObjectiveQualifiers(profile);
    }

    private String rewriteByTags(String name, QualifiersMatcher.MatchResult<QualifierConfig> matchResult) {
        for (Tag tag : matchResult.value().tags()) {
            name = switch (tag) {
                case QualifiersTag qpa -> rewriteByQualifiers(name, qpa, matchResult.subjectQualifiers());
                case SubstituteTag sa -> rewriteBySubstitute(name, sa);
                default -> throw new ResourceException("Unsupported template tag. Resource name template=" + name + "; Tag=" + tag);
            };
        }
        return name;
    }

    private String rewriteByQualifiers(String name, QualifiersTag tag, SubjectQualifiers qualifiers) {
        String suffix = qualifiers.toSuffix();
        String partLeft = StringUtils.substring(name, 0, tag.position());
        String partRight = StringUtils.substring(name, tag.position());
        return partLeft + suffix + partRight;
    }

    private String rewriteBySubstitute(String name, SubstituteTag tag) {
        String partLeft = StringUtils.substring(name, 0, tag.startPosition());
        String partRight = StringUtils.substring(name, tag.endPosition());
        return partLeft + tag.substitution() + partRight;
    }

    /**
     * Config associated with resource base name
     */
    record NameConfig(QualifiersMatcher<QualifierConfig> matcher) {

    }

    /**
     * Config associated with qualifier
     */
    record QualifierConfig(Tag[] tags) {

    }
}
