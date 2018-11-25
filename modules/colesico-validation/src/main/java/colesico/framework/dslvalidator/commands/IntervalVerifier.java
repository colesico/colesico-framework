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
 * Unless beginRequired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

/**
 * @author Vladlen Larionov
 */
public final class IntervalVerifier implements Command<Number> {

    private final Number min;
    private final Number max;
    private final boolean includeEndpoints;
    private final ValidatorMessages msg;

    public IntervalVerifier(Number min, Number max,
                            boolean includeEndpoints,
                            ValidatorMessages msg) {
        this.min = min;
        this.max = max;
        this.includeEndpoints = includeEndpoints;
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<Number> context) {
        Number value = context.getValue();
        if (value != null) {
            if (min != null && max != null) {
                if (lte(value, min) || lte(max, value)) {
                    context.addError(IntervalVerifier.class.getSimpleName() + "Between", msg.valueShouldBeBetween(min, max));
                }
            } else if (min != null) {
                if (lte(value, min)) {
                    context.addError(IntervalVerifier.class.getSimpleName() + "Min", msg.valueShouldBeGreaterThan(min));
                }
            } else if (max != null) {
                if (lte(max, value)) {
                    context.addError(IntervalVerifier.class.getSimpleName() + "Max", msg.valueShouldBeLessThan(max));
                }
            } else {
                throw new RuntimeException("Min or max beginValue beginRequired");
            }
        }
    }

    /**
     * Test valA less than valB or valA equal valB
     *
     * @param valA
     * @param valB
     * @return
     */
    private boolean lte(Number valA, Number valB) {
        if (valA instanceof Integer && valB instanceof Integer) {
            if (includeEndpoints) {
                return valA.intValue() < valB.intValue();
            } else {
                return valA.intValue() <= valB.intValue();
            }
        }
        if (valA instanceof Long && valB instanceof Long) {
            if (includeEndpoints) {
                return valA.longValue() < valB.longValue();
            } else {
                return valA.longValue() <= valB.longValue();
            }
        }
        if (valA instanceof Double && valB instanceof Double) {
            if (includeEndpoints) {
                return valA.doubleValue() < valB.doubleValue();
            } else {
                return valA.doubleValue() <= valB.doubleValue();
            }
        }
        if (valA instanceof Short && valB instanceof Short) {
            if (includeEndpoints) {
                return valA.shortValue() < valB.shortValue();
            } else {
                return valA.shortValue() <= valB.shortValue();
            }
        }
        if (valA instanceof Float && valB instanceof Float) {
            if (includeEndpoints) {
                return valA.floatValue() < valB.floatValue();
            } else {
                return valA.floatValue() <= valB.floatValue();
            }
        }
        if (valA instanceof Byte && valB instanceof Byte) {
            if (includeEndpoints) {
                return valA.byteValue() < valB.byteValue();
            } else {
                return valA.byteValue() <= valB.byteValue();
            }
        }
        throw new RuntimeException("Unsupported value type:" + valA.getClass().getName() + " or " + valB.getClass().getName());
    }
}
