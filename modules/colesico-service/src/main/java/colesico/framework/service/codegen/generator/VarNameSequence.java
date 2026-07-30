package colesico.framework.service.codegen.generator;

/**
 * Temporary variable names generator
 */
public class VarNameSequence {

    public final String defaultPrefix;

    public VarNameSequence(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    private int idx = 0;

    public String nextName() {
        return defaultPrefix + (idx++);
    }

    public String nextName(String namePrefix) {
        return namePrefix + (idx++);
    }

    public void reset() {
        idx = 0;
    }
}
