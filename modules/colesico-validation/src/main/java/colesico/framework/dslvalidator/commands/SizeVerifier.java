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

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

import java.util.Collection;
import java.util.Map;

public class SizeVerifier<V> extends AbstractIntervalVerifier<V> {

    protected final ValidatorMessages msg;

    public SizeVerifier(Number min, Number max, boolean includeEndpoints, ValidatorMessages msg) {
        super(min, max, includeEndpoints);
        this.msg = msg;
    }


    @Override
    protected String valueShouldBeBetween(Number min, Number max) {
        return null;
    }

    @Override
    protected String valueShouldBeGreaterThan(Number min) {
        return null;
    }

    @Override
    protected String valueShouldBeLessThan(Number max) {
        return null;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        Object value = context.getValue();
        Number size;

        if (value == null) {
            size = 0;
        } else if (value instanceof Collection) {
            size = ((Collection) value).size();
        } else if (value instanceof Map) {
            size = ((Map) value).size();
        } else if (value.getClass().isArray()) {
            size = ((Object[]) context.getValue()).length;
        } else if (value instanceof String) {
            size = ((String)value).length();
        } else {
            throw new RuntimeException("Unsupported value type: " + value.getClass().getName());
        }

        execute(size, context);
    }
}
