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

package colesico.framework.jdbi;

import colesico.framework.jdbi.internal.JdbiTransaction;
import colesico.framework.transaction.AbstractTransactionalShell;
import colesico.framework.transaction.Tuning;
import colesico.framework.transaction.UnitOfWork;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * Jdbi based transactional shell
 */
public class JdbiTransactionalShell extends AbstractTransactionalShell<JdbiTransaction, Tuning<Handle>> {

    protected final Jdbi jdbi;

    public JdbiTransactionalShell(Jdbi jdbi) {
        super(LoggerFactory.getLogger(JdbiTransactionalShell.class));
        this.jdbi = jdbi;
    }

    @Override
    public void setRollbackOnly() {
        JdbiTransaction tx = getTransaction();
        tx.setRollbackOnly(true);
    }

    @Override
    protected <R> R createNew(UnitOfWork<R> unitOfWork, Tuning<Handle> tuning) {
        logger.debug("TX-New (JDBI) begin");
        if (transactions.get() != null) {
            throw new IllegalStateException("Active Jdbi transaction exists");
        }
        JdbiTransaction tx = new JdbiTransaction();
        transactions.set(tx);

        Handle handle = null;
        try {
            R result = unitOfWork.execute();
            handle = tx.getHandle();
            if (handle != null) {
                if (tx.getRollbackOnly()) {
                    handle.rollback();
                } else {
                    handle.commit();
                }
            }
            return result;
        } catch (Exception e) {
            if (handle != null) {
                try {
                    handle.rollback();
                } catch (Exception rbe) {
                    logger.error("Error rolling back Jdbi connection: " + ExceptionUtils.getRootCauseMessage(rbe));
                }
            }
            throw rethrow(e);
        } finally {
            if (handle != null) {
                try {
                    handle.close();
                } catch (Exception e) {
                    logger.error("Error closing jDBI connection: " + ExceptionUtils.getRootCauseMessage(e));
                }
            }
            transactions.remove();
            logger.debug("TX-New (JDBI) end");
        }
    }

    /**
     * Return active Jdbi handle bound to active transaction
     *
     * @return Jdbi handle
     */
    public Handle getHandle() {
        JdbiTransaction tx = getTransaction();
        Handle handle = tx.getHandle();
        if (handle == null) {
            handle = jdbi.open();
            handle.begin();
            if (tx.getTuning() != null) {
                tx.getTuning().applyTuning(handle);
            }
            tx.setHandle(handle);
        }
        return handle;
    }

    /**
     * Get shell bound jdbi instance
     *
     * @return
     */
    public Jdbi getJdbi() {
        return jdbi;
    }

    /**
     * Return underlying jdbc connection bound to active transaction
     *
     * @return
     */
    public Connection getConnection() {
        return getHandle().getConnection();
    }

}
