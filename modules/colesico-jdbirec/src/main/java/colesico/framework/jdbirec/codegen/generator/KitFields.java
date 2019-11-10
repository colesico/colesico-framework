/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
