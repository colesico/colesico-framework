package colesico.framework.resource.l10n;

/**
 * Localization tool
 */
public interface Localizer {

    /**
     * Return localized path
     */
    String localize(String path);

    String[] localizeInheritance(String path);
}
