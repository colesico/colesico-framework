package colesico.framework.resource.rewriters.localization;

import colesico.framework.profile.Profile;
import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.RewritingPhase;
import colesico.framework.resource.assist.FileParser;
import colesico.framework.resource.assist.PathTrie;
import colesico.framework.resource.assist.localization.Matcher;
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
public class L10nRewriter implements PathRewriter, L10NRewriterSettings {

    private static final Logger log = LoggerFactory.getLogger(L10nRewriter.class);

    private final PathTrie<L10NConfig> pathTrie = PathTrie.of();

    private final L10nRewriterConfigPrototype config;
    private final Provider<Profile> profileProv;

    @Inject
    public L10nRewriter(L10nRewriterConfigPrototype config, Provider<Profile> profileProv) {
        this.config = config;
        this.profileProv = profileProv;
    }

    @Override
    public RewritingPhase phase() {
        return RewritingPhase.LOCALIZE;
    }

    /**
     * Register localizing qualifiers for specific resource path
     *
     * @param path                  - resource path
     * @param mode                  - rewriting mode
     * @param subjectQualifiersSpec - subject qualifiers values set specification string in the format: qualifierName1=value1;qualifierName2=value2...
     *                              Qualifier values order is unimportant, it will be ordered at parsing time
     */
    public L10nRewriter l10n(String path, L10nMode mode, String... subjectQualifiersSpec) {
        final PathTrie.Node<L10NConfig> node = pathTrie.add(path);
        L10NConfig l10NConfig = node.getValue();
        Matcher matcher;
        if (l10NConfig == null) {
            l10NConfig = new L10NConfig();
            matcher = new Matcher();
            l10NConfig.setLocalizer(matcher);
            l10NConfig.setMode(mode);
            node.setValue(l10NConfig);
        } else {
            matcher = l10NConfig.getLocalizer();
            if (!mode.equals(l10NConfig.getMode())) {
                throw new ResourceException("Localization mode mismatch");
            }
        }

        for (String qualifiersSpecItem : subjectQualifiersSpec) {
            matcher.addQualifiers(SubjectQualifiers.ofSpec(qualifiersSpecItem, config.getQualifiersDefinition()));
        }
        return this;
    }

    public L10nRewriter l10n(Class clazz, L10nMode mode, String... subjectQualifiersSpec) {
        return l10n(clazz.getCanonicalName().replace('.', '/'), mode, subjectQualifiersSpec);
    }

    @Override
    public String rewrite(final String path) {

        L10NConfig l10NConfig = pathTrie.find(path);

        if (l10NConfig == null) {
            return path;
        }

        if (l10NConfig.getMode().equals(L10nMode.NONE)) {
            return path;
        }

        SubjectQualifiers qualifiers = l10NConfig.getLocalizer().match(config.getObjectiveQualifiers(profileProv.get()));

        if (qualifiers == null) {
            return path;
        }

        switch (l10NConfig.getMode()) {
            case FILE:
                return rewriteFileName(path, qualifiers);
            case DIR:
                return rewriteDirectoryName(path, qualifiers);
            default:
                throw new ResourceException("Unsupported localization mode: " + l10NConfig.getMode());
        }
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
    private static class L10NConfig {

        private Matcher matcher;
        private L10nMode mode;

        public Matcher getLocalizer() {
            return matcher;
        }

        public L10nMode getMode() {
            return mode;
        }

        public void setLocalizer(Matcher matcher) {
            this.matcher = matcher;
        }

        public void setMode(L10nMode mode) {
            this.mode = mode;
        }
    }


}
