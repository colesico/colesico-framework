/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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
 */

package colesico.framework.jdbi.internal;

import colesico.framework.transaction.Tuning;
import org.jdbi.v3.core.Handle;

public class JdbiTransaction {

    private Handle handle;
    private Tuning<Handle> tuning;
    private boolean rollbackOnly = false;

    public Handle getHandle() {
        return handle;
    }

    public JdbiTransaction setHandle(Handle handle) {
        this.handle = handle;
        return this;
    }

    public Tuning<Handle> getTuning() {
        return tuning;
    }

    public JdbiTransaction setTuning(Tuning<Handle> tuning) {
        this.tuning = tuning;
        return this;
    }

    public boolean getRollbackOnly() {
        return rollbackOnly;
    }

    public void setRollbackOnly(boolean rollbackOnly) {
        this.rollbackOnly = rollbackOnly;
    }
}
