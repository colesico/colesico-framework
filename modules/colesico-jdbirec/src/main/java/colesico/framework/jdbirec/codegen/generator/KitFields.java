package colesico.framework.jdbirec.codegen.generator;

import javax.lang.model.type.TypeMirror;
import java.util.LinkedHashMap;
import java.util.Map;

public class KitFields {

    private Map<TypeMirror, String> fieldsMap = new LinkedHashMap<>();

    private final String namePrefix;
    private int index = 0;

    public KitFields(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    private String nextFieldName() {
        String fieldName = namePrefix + index;
        index++;
        return fieldName;
    }

    public String addField(TypeMirror fieldType) {
        String fieldName = fieldsMap.get(fieldType);
        if (fieldName != null) {
            return fieldName;
        }

        fieldName = nextFieldName();
        fieldsMap.put(fieldType, fieldName);
        return fieldName;
    }

    public Map<TypeMirror, String> getFieldsMap() {
        return fieldsMap;
    }

    public String getFieldName(TypeMirror type) {
        return fieldsMap.get(type);
    }
}
