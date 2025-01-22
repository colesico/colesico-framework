package colesico.framework.resource.rewriters.localization;

import colesico.framework.resource.ResourceException;
import colesico.framework.resource.assist.localization.Qualifier;
import colesico.framework.resource.assist.localization.QualifiersDefinition;
import colesico.framework.resource.assist.localization.SubjectQualifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class L10nRewriterSettings {

    private Map<String, PathSettings> pathSettings;

    private PathSettings settings;

    /**
     * Set current configuring path
     */
    public L10nRewriterSettings path(String path) {
        settings = pathSettings.computeIfAbsent(path, PathSettings::new);
        return this;
    }

    public L10nRewriterSettings clazz(Class clazz) {
        return path(clazz.getCanonicalName().replace('.', '/'));
    }

    /**
     * Set mode for current path
     */
    public L10nRewriterSettings mode(L10nMode mode) {
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
    public L10nRewriterSettings qualifiers(Qualifier... qualifiers) {
        settings.qualifiers.add(qualifiers);
        return this;
    }

    /**
     * Specify qualifiers via name1, value1, name2, value2 ...
     */
    public L10nRewriterSettings qualifiers(String... nv) {
        int length = nv.length / 2;
        Qualifier[] qualifiers = new Qualifier[length];
        for (int i = 0; i < length; i++) {
            Qualifier qualifier = Qualifier.of(nv[i], nv[i + 1]);
            qualifiers[i] = qualifier;
        }
        return qualifiers(qualifiers);
    }

    protected Collection<PathSettings> pathConfigs() {
        return pathSettings.values();
    }

    public static class PathSettings {
        protected String path;
        protected L10nMode mode;
        protected List<Qualifier[]> qualifiers = new ArrayList<>();

        public PathSettings(String path) {
            this.path = path;
        }

        public SubjectQualifiers[] qualifiers(QualifiersDefinition definition) {
            int n = qualifiers.size();
            SubjectQualifiers[] result = new SubjectQualifiers[n];
            for (int i = 0; i < n; i++) {
                Qualifier[] q = qualifiers.get(i);
                SubjectQualifiers sq = SubjectQualifiers.of(definition.toValues(q));
                result[i] = sq;
            }
            return result;
        }
    }
}
