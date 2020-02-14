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

package colesico.framework.example.transaction;

import colesico.framework.transaction.TransactionalShell;
import colesico.framework.transaction.UnitOfWork;

/**
 * Transactional shell mock that demonstrates how the actual transactional shell can be implemented
 */
public class TransctionalShellMock implements TransactionalShell<Object> {

    TextBuffer out = TextBuffer.INSTANCE;

    final String shellName;

    public TransctionalShellMock(String shellName) {
        this.shellName = shellName;
    }

    public TextBuffer getOut() {
        return out;
    }

    @Override
    public <R> R required(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("Required-begin-" + shellName );
        R result = unitOfWork.execute();
        out.append("Required-end-" + shellName );
        return result;
    }

    @Override
    public <R> R requiresNew(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("RequiresNew-begin-" + shellName );
        R result = unitOfWork.execute();
        out.append("RequiresNew-end-" + shellName );
        return result;
    }

    @Override
    public <R> R mandatory(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("Mandatory-begin-" + shellName );
        R result = unitOfWork.execute();
        out.append("Mandatory-end-" + shellName );
        return result;
    }

    @Override
    public <R> R notSupported(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("NotSupported-begin-" + shellName );
        R result = unitOfWork.execute();
        out.append("NotSupported-end-" + shellName );
        return result;
    }

    @Override
    public <R> R supports(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("Supports-begin-" + shellName );
        R result = unitOfWork.execute();
        out.append("Supports-end-" + shellName );
        return result;
    }

    @Override
    public <R> R never(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("Never-begin-" + shellName );
        R result = unitOfWork.execute();
        out.append("Never-end-" + shellName );
        return result;
    }

    @Override
    public <R> R nested(UnitOfWork<R> unitOfWork, Object tuning) {
        out.append("Nested begin");
        R result = unitOfWork.execute();
        out.append("Nested end");
        return result;
    }

    @Override
    public void setRollbackOnly() {
        throw new UnsupportedOperationException("Not supported");
    }
}
