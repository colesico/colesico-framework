package colesico.framework.resource.localization;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.ResourceException;

import java.util.*;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class L10nOptionsPrototype {
    /**
     * To use in code generators
     */
    public static final String CONFIGURE_METHOD = "configure";
    public static final String OPTIONS_PARAM = "options";

    abstract public void configure(Options options);

    public static final class Options {

        public static final String PATH_METHOD = "path";
        public static final String QUALIFIERS_METHOD = "qualifiers";
        public static final String QUALIFIER_METHOD = "qualifier";
        public static final String LANGUAGE_METHOD = "language";
        public static final String COUNTRY_METHOD = "country";
        public static final String VARIANT_METHOD = "variant";

        private final Map<String, PathOptions> pathsOptions = new HashMap<>();

        // Current path options
        private PathOptions options;

        // Current qualifiers set
        private Set<Qualifier> qualifiers;

        /**
         * Register path localization rewriting for specific resource path and qualifiers
         * The following placeholders can be used in the path template:
         * -   {Q}  qualifiers suffix placeholder i.e. messages{Q}.txt  messages_ru_RU.txt
         * -   {{str=substitution}} path part for substitution :  /app/{{module=ext}}/messages.txt -> /app/ext/messages.txt
         */
        public Options path(String path) {
            options = pathsOptions.computeIfAbsent(path, PathOptions::new);
            qualifiers = null;
            return this;
        }

        /**
         * Set current configuration path via class
         */
        public Options clazz(Class clazz) {
            return path(clazz.getCanonicalName().replace('.', '/'));
        }

        /**
         * Starts qualifiers set for current path
         * Qualifiers order is unimportant, it will be ordered at parsing time
         */
        public Options qualifiers() {
            qualifiers = new HashSet<>();
            options.qualifiers.add(qualifiers);
            return this;
        }

        public Options qualifier(Qualifier qualifier) {
            if (qualifiers == null) {
                throw new ResourceException("Start qualifiers set with qualifiers()");
            }
            qualifiers.add(qualifier);
            return this;
        }

        /**
         * Specify language qualifier
         */
        public Options language(String lang) {
            return qualifier(Qualifier.language(lang));
        }

        /**
         * Specify country qualifier
         */
        public Options country(String country) {
            return qualifier(Qualifier.country(country));
        }

        /**
         * Specify variant qualifier
         */
        public Options variant(String variant) {
            return qualifier(Qualifier.variant(variant));
        }

        public Collection<PathOptions> pathSettings() {
            return pathsOptions.values();
        }

    }

    public static class PathOptions {
        private final String path;
        private final List<Set<Qualifier>> qualifiers = new ArrayList<>();

        public PathOptions(String path) {
            this.path = path;
        }

        public String path() {
            return path;
        }

        public SubjectQualifiers[] qualifiers(QualifiersDefinition definition) {
            int n = qualifiers.size();
            SubjectQualifiers[] result = new SubjectQualifiers[n];
            for (int i = 0; i < n; i++) {
                Set<Qualifier> q = qualifiers.get(i);
                SubjectQualifiers sq = SubjectQualifiers.of(definition, q.toArray(Qualifier[]::new));
                result[i] = sq;
            }
            return result;
        }
    }
}
