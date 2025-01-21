package colesico.framework.resource.assist.localization;

public record Qualifier(String name, String value) {

    public static String LANGUAGE_QUALIFIER = "language";
    public static String COUNTRY_QUALIFIER = "country";
    public static String VARIANT_QUALIFIER = "variant";

    public static Qualifier ofLanguage(String lang) {
        return new Qualifier(LANGUAGE_QUALIFIER, lang);
    }

    public static Qualifier ofCountry(String country) {
        return new Qualifier(COUNTRY_QUALIFIER, country);
    }

    public static Qualifier ofVariant(String variant) {
        return new Qualifier(VARIANT_QUALIFIER, variant);
    }
}
