package colesico.framework.resource.l10n;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.resource.ResourceException;
import org.apache.commons.lang3.StringUtils;

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

        public static final String BASE_NAME_METHOD = "baseName";
        public static final String BASE_CLASS_METHOD = "baseClass";
        public static final String QUALIFIERS_METHOD = "qualifiers";
        public static final String QUALIFIER_METHOD = "qualifier";
        public static final String LANGUAGE_METHOD = "language";
        public static final String COUNTRY_METHOD = "country";
        public static final String VARIANT_METHOD = "variant";

        private final Map<String, NameOptions> baseNameOptions = new HashMap<>();

        // Current resource name options
        private NameOptions options;

        // Current qualifiers set
        private Set<Qualifier> qualifiers;

        /**
         * Register resource name localization  for specific resource base name and qualifiers
         * The following placeholders can be used in the resource name template:
         * -   {Q}  qualifiers suffix placeholder i.e. messages{Q}.txt  messages_ru_RU.txt
         * -   {{str=substitution}} resource name part for substitution :  /app/{{module=ext}}/messages.txt -> /app/ext/messages.txt
         */
        public Options baseName(String baseName) {
            options = baseNameOptions.computeIfAbsent(baseName, NameOptions::new);
            qualifiers = null;
            return this;
        }

        /**
         * Set current configuration resource name via class
         */
        public Options baseClass(Class baseClass, String searchString, String substitution) {
            String baseName = baseClass.getCanonicalName();
            baseName = StringUtils.replace(baseName, searchString, "{{" + searchString + "=" + substitution + "}}");
            return baseName(baseName.replace('.', '/') + "{Q}");
        }

        public Options baseClass(Class baseClass) {
            String baseName = baseClass.getCanonicalName();
            return baseName(baseName.replace('.', '/') + "{Q}");
        }

        /**
         * Starts qualifiers set for current resource base name
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

        public Collection<NameOptions> baseNameOptions() {
            return baseNameOptions.values();
        }

    }

    public static class NameOptions {
        private final String name;
        private final List<Set<Qualifier>> qualifiers = new ArrayList<>();

        public NameOptions(String name) {
            this.name = name;
        }

        public String name() {
            return name;
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
