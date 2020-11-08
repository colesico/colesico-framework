package colesico.framework.resource;

/**
 * Rewriters applying order
 */
public enum RewritingPhase {

    /**
     * Phase to evaluate path expressions
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
     * Phase to substitute/redirect path
     */
    BEFORE_SUBSTITUTE,
    SUBSTITUTE,
    AFTER_SUBSTITUTE
}


