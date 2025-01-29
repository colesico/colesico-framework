package colesico.framework.resource;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.assist.localization.Qualifier;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.assist.localization.SubjectQualifiers;

import java.util.*;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ResourceL10nOptionsPrototype {
    /**
     * To use in code generators
     */
    public static final String CONFIGURE_METHOD = "configure";
    public static final String OPTIONS_PARAM = "options";

    abstract public void configure(Options options);

    public static final class Options {

        private final Map<String, PathOptions> pathsOptions = new HashMap<>();

        // Current path options
        private PathOptions options;

        // Current qualifiers set
        private Set<Qualifier> qualifiers;

        /**
         * Set current configuring path
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
