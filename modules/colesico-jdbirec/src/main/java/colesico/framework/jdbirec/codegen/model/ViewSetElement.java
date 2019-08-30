package colesico.framework.jdbirec.codegen.model;

import java.util.HashMap;
import java.util.Map;

public class ViewSetElement {

    protected final Map<String, RecordElement> records = new HashMap<>();

    public Map<String, RecordElement> getRecords() {
        return records;
    }

    public void addRecord(String view, RecordElement record) {
        records.put(view, record);
    }
}
