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

package colesico.framework.jdbi;

import colesico.framework.jdbi.internal.JdbiTransaction;
import colesico.framework.transaction.AbstractTransactionalShell;
import colesico.framework.transaction.Transaction;
import colesico.framework.transaction.Tuning;
import colesico.framework.transaction.UnitOfWork;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Random;

/**
 * Jdbi based transactional shell
 */
public class JdbiTransactionalShell extends AbstractTransactionalShell<JdbiTransaction, Tuning<Handle>> {

    /**
     * To generate Transaction.Id
     */
    private static Random random = new Random();

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
        JdbiTransaction tx = transactions.get();
        if (tx != null) {
            throw new IllegalStateException("Active Jdbi transaction exists; txId:" + getTxId(tx));
        }

        String txId = Long.toHexString(System.currentTimeMillis()) + ':' + Long.toHexString(random.nextLong());
        logger.debug("TX-New (JDBI) txId: {}", txId);
        tx = new JdbiTransaction()
                .setTuning(tuning)
                .setId(txId);

        transactions.set(tx);

        Handle handle = null;
        R result = null;
        try {
            try {
                result = unitOfWork.execute();
            } finally {
                handle = tx.getHandle();
            }

            if (handle != null) {
                if (tx.getRollbackOnly()) {
                    logger.debug("TX-New (JDBI) rollback JDBI handle");
                    handle.rollback();
                } else {
                    logger.debug("TX-New (JDBI) commit JDBI handle");
                    handle.commit();
                }
            } else {
                logger.debug("TX-New (JDBI) close JDBI handle is null");
            }
        } catch (Exception e) {
            logger.debug("TX-New (JDBI) exception:" + ExceptionUtils.getRootCauseMessage(e));

            if (handle != null) {
                try {
                    logger.debug("TX-New (JDBI) rollback JDBI handle");
                    handle.rollback();
                } catch (Exception rbe) {
                    logger.error("Error rolling back Jdbi connection: " + ExceptionUtils.getRootCauseMessage(rbe));
                }
            } else {
                logger.debug("TX-New (JDBI) close JDBI handle is null");
            }
            rethrow(e);
        } finally {

            if (handle != null) {
                try {
                    logger.debug("TX-New (JDBI) close JDBI handle");
                    handle.close();
                } catch (Exception e) {
                    logger.error("Error closing JDBI connection: " + ExceptionUtils.getRootCauseMessage(e));
                }
            } else {
                logger.debug("TX-New (JDBI) close JDBI handle is null");
            }
            transactions.remove();
            logger.debug("TX-New (JDBI) end");
        }
        return result;
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
