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
public class PathLocalizer {

    private static final Logger log = LoggerFactory.getLogger(PathLocalizer.class);

    private final PathTrie<PathConfig> pathTrie = PathTrie.of();

    private final L10nConfigPrototype config;
    private final Provider<Profile> profileProv;

    @Inject
    public PathLocalizer(L10nConfigPrototype config,
                         Polysupplier<L10nOptionsPrototype> options,
                         Provider<Profile> profileProv) {
        this.config = config;
        this.profileProv = profileProv;

        var opt = new L10nOptionsPrototype.Options();
        options.forEach(o -> o.configure(opt));

        QualifiersDefinition definition = config.getQualifiersDefinition();
        for (var pc : opt.pathSettings()) {
            addLocalization(pc.path(), pc.qualifiers(definition));
        }
    }

    /**
     * Register path localization  for specific resource.
     * The following placeholders can be used in the path template:
     * -   {Q}  qualifiers suffix placeholder i.e. messages{Q}.txt  messages_ru_RU.txt
     * -   {{str=substitution}} path part for substitution :  /app/{module>ext}/messages.txt -> /app/ext/messages.txt
     *
     * @param pathTemplate      - resource path template
     * @param subjectQualifiers - subject qualifiers values
     */
    private void addLocalization(String pathTemplate,
                                 SubjectQualifiers[] subjectQualifiers) {

        PathTemplateParser pathTemplateParser = PathTemplateParser.parse(pathTemplate);
        String path = pathTemplateParser.getPath();

        final PathTrie.Node<PathConfig> node = pathTrie.add(path);
        PathConfig pathConfig = node.getValue();
        QualifiersMatcher<QualifierConfig> matcher;
        if (pathConfig == null) {
            matcher = new QualifiersMatcher<>();
            pathConfig = new PathConfig(matcher);
            node.setValue(pathConfig);
        } else {
            matcher = pathConfig.matcher();
        }

        PathTag[] tags = pathTemplateParser.getTags();
        for (SubjectQualifiers sq : subjectQualifiers) {
            var prvRewriting = matcher.addQualifiers(sq, new QualifierConfig(tags));
            if (prvRewriting != null) {
                throw new ResourceException("Path rewriting already defined. Path=" + path + "; Qualifiers=" + sq);
            }
        }

    }

    /**
     * Returns best suitable for current profile localized resource path
     *
     * @param path resource base path
     */
    public String localization(String path) {

        PathConfig rewriting = pathTrie.find(path);

        if (rewriting == null) {
            return path;
        }

        var profile = profileProv.get();
        var matchResult = rewriting.matcher().match(config.getObjectiveQualifiers(profile));

        if (matchResult == null) {
            return path;
        }

        return rewriteByTags(path, matchResult);
    }

    /**
     * Returns suitable localizations for path.
     * The first path corresponds to the result of the method {@link #localization(String)} - the most suitable localization
     * The last one path - default localization  (less suitable)
     *
     * @param path resource base path
     */
    public String[] suitableLocalizations(String path) {
        PathConfig rewriting = pathTrie.find(path);

        if (rewriting == null) {
            return new String[]{path};
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
            String rewritedPath = rewriteByTags(path, matchResult);
            result.add(rewritedPath);

            // Reduce qualifiers significant capacity
            String[] values = objectiveQualifiers.getValues();
            values[n - i - 1] = null;
            objectiveQualifiers = ObjectiveQualifiers.of(values);
        }

        if (result.isEmpty()) {
            return new String[]{path};
        }

        return result.toArray(String[]::new);
    }

    private String rewriteByTags(String path, QualifiersMatcher.MatchResult<QualifierConfig> matchResult) {
        for (PathTag tag : matchResult.value().tags()) {
            path = switch (tag) {
                case QualifiersTag qpa -> rewriteByQualifiers(path, qpa, matchResult.subjectQualifiers());
                case SubstituteTag sa -> rewriteBySubstitute(path, sa);
                default -> throw new ResourceException("Unsupported path tag. Path=" + path + "; Tag=" + tag);
            };
        }
        return path;
    }

    private String rewriteByQualifiers(String path, QualifiersTag tag, SubjectQualifiers qualifiers) {
        String suffix = qualifiers.toSuffix();
        String partLeft = StringUtils.substring(path, 0, tag.position());
        String partRight = StringUtils.substring(path, tag.position());
        return partLeft + suffix + partRight;
    }

    private String rewriteBySubstitute(String path, SubstituteTag tag) {
        String partLeft = StringUtils.substring(path, 0, tag.startPosition());
        String partRight = StringUtils.substring(path, tag.endPosition());
        return partLeft + tag.substitution() + partRight;
    }

    /**
     * Config associated with path
     */
    record PathConfig(QualifiersMatcher<QualifierConfig> matcher) {

    }

    /**
     * Config associated with qualifier
     */
    record QualifierConfig(PathTag[] tags) {

    }
}
