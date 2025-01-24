package colesico.framework.resource.rewriters.localization;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.Profile;
import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.RewritingPhase;
import colesico.framework.resource.assist.FileParser;
import colesico.framework.resource.assist.PathTrie;
import colesico.framework.resource.assist.localization.Matcher;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.assist.localization.SubjectQualifiers;
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

    private final PathTrie<PathL10n> pathTrie = PathTrie.of();

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
     * Register localizing qualifiers for specific resource path
     *
     * @param path              - resource path
     * @param mode              - rewriting mode
     * @param subjectQualifiers - subject qualifiers values
     */
    private void pathLocalization(String path, L10nMode mode, SubjectQualifiers... subjectQualifiers) {
        final PathTrie.Node<PathL10n> node = pathTrie.add(path);
        PathL10n pathL10N = node.getValue();
        Matcher<X> matcher;
        if (pathL10N == null) {
            matcher = new Matcher();
            pathL10N = new PathL10n(matcher, mode);
            node.setValue(pathL10N);
        } else {
            matcher = pathL10N.matcher();
            if (!mode.equals(pathL10N.mode())) {
                throw new ResourceException("Localization mode mismatch");
            }
        }

        for (SubjectQualifiers sq : subjectQualifiers) {
            matcher.addQualifiers(sq, );
        }

    }

    @Override
    public String rewrite(final String path) {

        PathL10n pathL10N = pathTrie.find(path);

        if (pathL10N == null) {
            return path;
        }

        if (pathL10N.mode().equals(L10nMode.NONE)) {
            return path;
        }

        var matchResult = pathL10N.matcher().match(config.getObjectiveQualifiers(profileProv.get()));

        if (matchResult == null) {
            return path;
        }

        return switch (pathL10N.mode()) {
            case FILE -> rewriteFileName(path, matchResult.subjectQualifiers());
            case DIR -> rewriteDirectoryName(path, matchResult.subjectQualifiers());
            default -> throw new ResourceException("Unsupported localization mode: " + pathL10N.mode());
        };
    }

    /**
     * valid:
     * root/dir/file.txt - > root/dir/file_en_GB.txt
     * root/dir/file - > root/dir/file_en_GB
     * root/dir -> root/dir_en_GB
     * file.txt -> file_en_GB.txt
     * file -> file_en_GB
     * <p>
     * invalid:
     * root/dir/ - > root/dir/_en_GB !
     * .txt - > .txt_en_GB !
     * /.txt - > /.txt_en_GB !
     * / -> /_en_GB !
     */
    private String rewriteFileName(String path, SubjectQualifiers qualifiers) {

        final FileParser fp = new FileParser(path);
        final String filePath = fp.path();
        final String fileName = fp.fileName();

        if ("".equals(fileName)) {
            throw new ResourceException("Empty file name: " + path);
        }

        final String fileExt = fp.extension();


        log.info("filePath: {}", filePath);
        log.info("fileName: {}", fileName);
        log.info("fileExt: {}", fileExt);


        final StringBuilder sb;
        if ("".equals(filePath)) {
            sb = new StringBuilder(fileName);
        } else {
            sb = new StringBuilder(filePath).append("/").append(fileName);
        }

        sb.append(qualifiers.toSuffix('_'));

        if (!"".equals(fileExt)) {
            sb.append('.').append(fileExt);
        }
        return sb.toString();
    }

    /**
     * root/dir/ - > root/dir_en_GB/
     * root/dir  - > root_en_GB/dir
     * root/dir/file - > root/dir_en_GB/file
     * root/dir/file.txt - > root/dir_en_GB/file.txt
     * file.txt - > _en_GB/file.txt
     * .txt - > _en_GB/.txt
     * /.txt - > _en_GB/.txt
     * / -> _en_GB/
     */
    private String rewriteDirectoryName(String resourcePath, SubjectQualifiers qualifiers) {
        final FileParser fp = new FileParser(resourcePath);
        final String path = fp.path();
        final String fileName = fp.fileName();
        final String fileExt = fp.extension();

        final StringBuilder sb;

        sb = new StringBuilder(path);

        sb.append(qualifiers.toSuffix('_'));

        sb.append("/").append(fileName);
        if (!"".equals(fileExt)) {
            sb.append('.').append(fileExt);
        }
        return sb.toString();
    }

    /**
     * Localization config associated with path
     */
    private record PathL10n(Matcher matcher, L10nMode mode) {

    }


}
