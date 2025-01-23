package colesico.framework.resource.rewriters.localization;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.assist.localization.Qualifier;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.assist.localization.SubjectQualifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class L10nOptionsPrototype {

    abstract public void configure(Options options);

    public static final class Options {

        private Map<String, PathSettings> pathSettings;

        private PathSettings settings;

        /**
         * Set current configuring path
         */
        public Options path(String path) {
            settings = pathSettings.computeIfAbsent(path, PathSettings::new);
            return this;
        }

        public Options clazz(Class clazz) {
            return path(clazz.getCanonicalName().replace('.', '/'));
        }

        /**
         * Set mode for current path
         */
        public Options mode(L10nMode mode) {
            if (settings.mode == null) {
                settings.mode = mode;
            } else {
                if (settings.mode != mode) {
                    throw new ResourceException("L10nMode mismatch for path: " + settings.path);
                }
            }
            return this;
        }

        /**
         * Specify qualifier for current path
         * Qualifiers order is unimportant, it will be ordered at parsing time
         */
        public Options qualifiers(Qualifier... qualifiers) {
            settings.qualifiers.add(qualifiers);
            return this;
        }

        /**
         * Specify qualifiers via name1, value1, name2, value2 ...
         */
        public Options qualifiers(String... nv) {
            int length = nv.length / 2;
            Qualifier[] qualifiers = new Qualifier[length];
            for (int i = 0; i < length; i++) {
                Qualifier qualifier = Qualifier.of(nv[i], nv[i + 1]);
                qualifiers[i] = qualifier;
            }
            return qualifiers(qualifiers);
        }

        public Collection<PathSettings> pathSettings() {
            return pathSettings.values();
        }


    }

    public static class PathSettings {
        private final String path;
        private L10nMode mode;
        private final List<Qualifier[]> qualifiers = new ArrayList<>();

        public PathSettings(String path) {
            this.path = path;
        }

        public String path() {
            return path;
        }

        public L10nMode mode(L10nMode defaultValue) {
            if (mode == null) {
                return defaultValue;
            }
            return mode;
        }

        public SubjectQualifiers[] qualifiers(QualifiersDefinition definition) {
            int n = qualifiers.size();
            SubjectQualifiers[] result = new SubjectQualifiers[n];
            for (int i = 0; i < n; i++) {
                Qualifier[] q = qualifiers.get(i);
                SubjectQualifiers sq = SubjectQualifiers.of(definition.canonicalize(q));
                result[i] = sq;
            }
            return result;
        }
    }
}
