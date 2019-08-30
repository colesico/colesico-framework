package colesico.framework.jdbirec.codegen.generator;

public class VarNames {
    private int index = 0;

    public String getNextVarName(String prefix) {
        return prefix + (index++);
    }
}
