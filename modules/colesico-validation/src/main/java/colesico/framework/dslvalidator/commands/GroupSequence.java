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

import colesico.framework.dslvalidator.Sequence;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes all sequence commands within the current context.
 * Commands execution is not interrupted when errors occur.
 *
 * @author Vladlen Larionov
 * @see ChainSequence
 */
public final class GroupSequence<V> extends AbstractSequence<V, V> {

    @Override
    public void execute(ValidationContext<V> context) {
       executeGroup(context);
    }

}
