/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.assist.codegen;

import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Array code generation helper
 * @author Vladlen Larionov
 */
public class ArrayCodegen {

    private final TypeName type;
    private final List<String> formatsList = new ArrayList<>();
    private final List<Object> valuesList = new ArrayList<>();

    public ArrayCodegen() {
        type = null;
    }

    public ArrayCodegen(TypeName type) {
        valuesList.add(type);
        this.type = type;
    }

    public void add(String format, Object... values) {
        formatsList.add(format);
        for (Object v : values) {
            valuesList.add(v);
        }
    }

    public void addAll(String format, List values) {
        for (Object val : values) {
            formatsList.add(format);
            valuesList.add(val);
        }
    }

    public void addAll(String format, List values, Function<Object, Object[]> splitter) {
        for (Object val : values) {
            formatsList.add(format);
            Object[] vals = splitter.apply(val);
            for (Object sv : vals) {
                valuesList.add(sv);
            }
        }
    }

    public void addAll(Function<Object, String> format, List values, Function<Object, Object[]> splitter) {
        for (Object val : values) {
            formatsList.add(format.apply(val));
            Object[] vals = splitter.apply(val);
            for (Object sv : vals) {
                valuesList.add(sv);
            }
        }
    }

    public String toFormat() {
        if (type != null) {
            return "new $T[]{" + StringUtils.join(formatsList, ",") + "}";
        } else {
            return StringUtils.join(formatsList, ",");
        }
    }


    public Object[] toValues() {
        return valuesList.toArray(new Object[valuesList.size()]);
    }
}
