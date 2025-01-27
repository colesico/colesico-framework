package colesico.framework.resource.internal.rewriters.localization;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.Profile;
import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.RewritingPhase;
import colesico.framework.resource.assist.PathTrie;
import colesico.framework.resource.assist.localization.Matcher;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.assist.localization.SubjectQualifiers;
import colesico.framework.resource.rewriters.localization.L10nConfigPrototype;
import colesico.framework.resource.rewriters.localization.L10nOptionsPrototype;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Localization rewriter
 */
@Singleton
public class L10nRewriter implements PathRewriter {

    private static final Logger log = LoggerFactory.getLogger(L10nRewriter.class);

    private final PathTrie<PathRewriting> pathTrie = PathTrie.of();

    private final L10nConfigPrototype config;
    private final Provider<Profile> profileProv;

    @Inject
    public L10nRewriter(L10nConfigPrototype config,
                        Polysupplier<L10nOptionsPrototype> options,
                        Provider<Profile> profileProv) {
        this.config = config;
        this.profileProv = profileProv;

        var opt = new L10nOptionsPrototype.Options();
        options.forEach(o -> o.configure(opt));

        QualifiersDefinition definition = config.getQualifiersDefinition();
        for (var pc : opt.pathSettings()) {
            pathLocalization(pc.path(), pc.mode(config.getDefaultMode()), pc.qualifiers(definition));
        }
    }

    @Override
    public RewritingPhase phase() {
        return RewritingPhase.LOCALIZE;
    }

    /**
     * Register path localization rewriting for specific resource path and qualifiers
     * The following placeholders can be used in the path template:
     * -   {Q_}  qualifiers prefix placeholder i.e. module/{Q_}messages.txt -> module/ru_RU_messages.txt
     * -   {_Q}  qualifiers suffix placeholder i.e. messages{_Q}.txt  messages_ru_RU.txt
     * -   {{path part}}  path part for replacement :  /app/{{module}}/messages.txt -> /app/ext/messages.txt
     *
     * @param pathTemplate      - resource path template
     * @param subjectQualifiers - subject qualifiers values
     */
    private void pathLocalization(String pathTemplate,
                                  SubjectQualifiers[] subjectQualifiers,
                                  String replacement) {
        final PathTrie.Node<PathRewriting> node = pathTrie.add(pathTemplate);
        PathRewriting rewriting = node.getValue();
        Matcher<QualifierRewriting> matcher;
        if (rewriting == null) {
            matcher = new Matcher<>();
            rewriting = new PathRewriting(matcher, mode);
            node.setValue(rewriting);
        } else {
            matcher = rewriting.matcher();
            if (!mode.equals(rewriting.mode())) {
                throw new ResourceException("Localization mode mismatch");
            }
        }

        for (SubjectQualifiers sq : subjectQualifiers) {
            matcher.addQualifiers(sq, );
        }

    }

    @Override
    public String rewrite(String path) {

        PathRewriting rewriting = pathTrie.find(path);

        if (rewriting == null) {
            return path;
        }

        var matchResult = rewriting.matcher().match(config.getObjectiveQualifiers(profileProv.get()));

        if (matchResult == null) {
            return path;
        }

        for (PathAction pathAction : matchResult.value().pathActions()) {
            path = switch (pathAction) {
                case QualifierSuffixAction qsa -> qualifierSuffixAction(path, qsa);
                case QualifierPrefixAction qpa -> qualifierPrefixAction(path, qpa);
                case SubstituteAction sa -> substituteAction(path, sa);
                default -> throw new ResourceException("Unsupported path action: " + path);
            };
        }

        return path;
    }


    private String qualifierSuffixAction(String path, QualifierSuffixAction action) {
        return path;
    }

    private String qualifierPrefixAction(String path, QualifierPrefixAction action) {
        return path;
    }

    private String substituteAction(String path, SubstituteAction action) {
        return path;
    }

    /**
     * Rewriting config associated with path
     */
    private record PathRewriting(Matcher<QualifierRewriting> matcher) {

    }

    /**
     * Rewriting config associated with qualifier
     */
    private record QualifierRewriting(PathAction[] pathActions) {

    }

}
