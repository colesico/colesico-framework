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

package colesico.framework.example.transaction;

import colesico.framework.transaction.TransactionalShell;
import colesico.framework.transaction.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transactional shell mock that demonstrates how the actual transactional shell can be implemented
 */
public class TransctionalShellMock implements TransactionalShell<Object> {

    Logger log = LoggerFactory.getLogger(TransctionalShellMock.class);

    final String shellName;

    public TransctionalShellMock(String shellName) {
        this.shellName = shellName;
    }

    @Override
    public <R> R required(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("Required begin |" + shellName);
        R result = unitOfWork.execute();
        log.info("Required end |" + shellName);
        return result;
    }

    @Override
    public <R> R requiresNew(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("RequiresNew begin |" + shellName);
        R result = unitOfWork.execute();
        log.info("RequiresNew end |" + shellName);
        return result;
    }

    @Override
    public <R> R mandatory(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("Mandatory begin");
        R result = unitOfWork.execute();
        log.info("Mandatory end");
        return result;
    }

    @Override
    public <R> R notSupported(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("NotSupported begin");
        R result = unitOfWork.execute();
        log.info("NotSupported end");
        return result;
    }

    @Override
    public <R> R supports(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("Supports begin");
        R result = unitOfWork.execute();
        log.info("Supports end");
        return result;
    }

    @Override
    public <R> R never(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("Never begin");
        R result = unitOfWork.execute();
        log.info("Never end");
        return result;
    }

    @Override
    public <R> R nested(UnitOfWork<R> unitOfWork, Object tuning) {
        log.info("Nested begin");
        R result = unitOfWork.execute();
        log.info("Nested end");
        return result;
    }

    @Override
    public void setRollbackOnly() {
        throw new UnsupportedOperationException("Not supported");
    }
}
