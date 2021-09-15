package colesico.framework.service.codegen.generator;

/**
 * Temporary variable names generator
 */
public class VarNameSequence {
    public static final String TMP_VAR = "var";

    private int tmpVarIndex = 0;

    public String getNextTempVariable() {
        return TMP_VAR + (tmpVarIndex++);
    }

    public String getNextTempVariable(String namePrefix) {
        return namePrefix + (tmpVarIndex++);
    }

    public void reset(){
        tmpVarIndex = 0;
    }
}
