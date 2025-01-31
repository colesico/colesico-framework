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

@Singleton
public class LocalizerImpl implements Localizer {

    private static final Logger log = LoggerFactory.getLogger(LocalizerImpl.class);

    private final PathTrie<PathRewriting> pathTrie = PathTrie.of();

    private final L10nConfigPrototype config;
    private final Provider<Profile> profileProv;

    @Inject
    public LocalizerImpl(L10nConfigPrototype config,
                         Polysupplier<L10nOptionsPrototype> options,
                         Provider<Profile> profileProv) {
        this.config = config;
        this.profileProv = profileProv;

        var opt = new L10nOptionsPrototype.Options();
        options.forEach(o -> o.configure(opt));

        QualifiersDefinition definition = config.getQualifiersDefinition();
        for (var pc : opt.pathSettings()) {
            addPathRewriting(pc.path(), pc.qualifiers(definition));
        }
    }


    /**
     * Register path localization rewriting for specific resource path and qualifiers
     * The following placeholders can be used in the path template:
     * -   {Q}  qualifiers suffix placeholder i.e. messages{Q}.txt  messages_ru_RU.txt
     * -   {{str=substitution}} path part for substitution :  /app/{module>ext}/messages.txt -> /app/ext/messages.txt
     *
     * @param pathTemplate      - resource path template
     * @param subjectQualifiers - subject qualifiers values
     */
    private void addPathRewriting(String pathTemplate,
                                  SubjectQualifiers[] subjectQualifiers) {

        PathTemplateParser pathTemplateParser = PathTemplateParser.parse(pathTemplate);
        String path = pathTemplateParser.getPath();

        final PathTrie.Node<PathRewriting> node = pathTrie.add(path);
        PathRewriting pathRewriting = node.getValue();
        Matcher<QualifierRewriting> matcher;
        if (pathRewriting == null) {
            matcher = new Matcher<>();
            pathRewriting = new PathRewriting(matcher);
            node.setValue(pathRewriting);
        } else {
            matcher = pathRewriting.matcher();
        }

        PathTag[] tags = pathTemplateParser.getTags();
        for (SubjectQualifiers sq : subjectQualifiers) {
            var prvRewriting = matcher.addQualifiers(sq, new QualifierRewriting(tags));
            if (prvRewriting != null) {
                throw new ResourceException("Path rewriting already defined. Path=" + path + "; Qualifiers=" + sq);
            }
        }

    }

    @Override
    public String rewrite(String path) {

        PathRewriting rewriting = pathTrie.find(path);

        if (rewriting == null) {
            return path;
        }

        var profile = profileProv.get();
        var matchResult = rewriting.matcher().match(config.getObjectiveQualifiers(profile));

        if (matchResult == null) {
            return path;
        }

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
     * Rewriting config associated with path
     */
    record PathRewriting(Matcher<QualifierRewriting> matcher) {

    }

    /**
     * Rewriting config associated with qualifier
     */
    record QualifierRewriting(PathTag[] tags) {

    }
}
