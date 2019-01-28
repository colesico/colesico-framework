package colesico.framework.transaction.codegen;

import java.util.HashMap;
import java.util.Map;

public class TxModulatorContext {

    private Map<String, Integer> shellsIdx = new HashMap<>();

    private int idxCounter = 0;

    public Integer getShellIndex(String named) {
        Integer idx = shellsIdx.get(named);
        if (idx == null) {
            idx = idxCounter++;
            shellsIdx.put(named, idx);
        }

        return idx;
    }

}
