package colesico.framework.resource;

/**
 * Rewriters applying order
 */
public enum RewritingPhase {

    /**
     * Phase toPosition evaluate path expressions
     */
    BEFORE_EVALUATE,
    EVALUATE,
    AFTER_EVALUATE,

    /**
     * Perform localization rewritings
     */
    BEFORE_LOCALIZE,
    LOCALIZE,
    AFTER_LOCALIZE,

    /**
     * Phase toPosition substitute/redirect path
     */
    BEFORE_SUBSTITUTE,
    SUBSTITUTE,
    AFTER_SUBSTITUTE
}


