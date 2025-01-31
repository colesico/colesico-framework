package colesico.framework.resource.localization;

import java.util.Objects;

/**
 * Configuration qualifier
 */
public record Qualifier(String name, String value) {

    public static String LANGUAGE_QUALIFIER = "language";
    public static String COUNTRY_QUALIFIER = "country";
    public static String VARIANT_QUALIFIER = "variant";

    public static Qualifier of(String name, String value) {
        return new Qualifier(name, value);
    }

    public static Qualifier language(String lang) {
        return new Qualifier(LANGUAGE_QUALIFIER, lang);
    }

    public static Qualifier country(String country) {
        return new Qualifier(COUNTRY_QUALIFIER, country);
    }

    public static Qualifier variant(String variant) {
        return new Qualifier(VARIANT_QUALIFIER, variant);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Qualifier qualifier = (Qualifier) o;
        return Objects.equals(name, qualifier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Qualifier{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
