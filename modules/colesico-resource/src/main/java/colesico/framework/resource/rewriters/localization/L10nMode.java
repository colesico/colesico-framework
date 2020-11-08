package colesico.framework.resource.rewriters.localization;

/**
 * Localization mode
 */
public enum L10nMode {

    /**
     * Doesn't use localization
     */
    NONE,

    /**
     * Localize as file, e.g. path/file.ext -> path/file_ru_RU.ext
     */
    FILE,

    /**
     * Localize as directory, e.g. path/file.ext -> path_en_UK/file.ext
     */
    DIR
}
