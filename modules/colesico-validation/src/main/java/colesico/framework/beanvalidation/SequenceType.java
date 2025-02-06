package colesico.framework.beanvalidation;

/**
 * Validation group type
 */
public enum SequenceType {
    GROUP("group"),
    OPTIONAL_GROUP("optionalGroup"),
    MANDATORY_GROUP("mandatoryGroup"),
    CHAIN("chain"),
    OPTIONAL_CHAIN("optionalChain"),
    MANDATORY_CHAIN("mandatoryChain"),
    CUSTOM("");

    private String methodName;

    SequenceType(String methodName) {
        this.methodName = methodName;
    }
}
