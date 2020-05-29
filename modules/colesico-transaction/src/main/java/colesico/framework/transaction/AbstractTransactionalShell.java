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

package colesico.framework.transaction;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

/**
 * Abstract common transactional shell functionality
 *
 * @param <T> transaction type
 * @param <U> tuning type
 */
abstract public class AbstractTransactionalShell<T extends Transaction, U> implements TransactionalShell<U> {

    protected final Logger logger;

    protected final ThreadLocal<T> transactions = new ThreadLocal<>();

    public AbstractTransactionalShell(Logger logger) {
        this.logger = logger;
    }

    abstract protected <R> R createNew(UnitOfWork<R> unitOfWork, U tuning);

    protected String getTxId(T transaction) {
        return transaction == null ? null : transaction.getId();
    }

    @Override
    public <R> R required(UnitOfWork<R> unitOfWork, U tuning) {
        T tx = transactions.get();
        if (logger.isDebugEnabled()) {
            logger.debug("TX-Required begin thId:{}", getTxId(tx));
        }
        R result = null;
        try {
            if (tx == null) {
                result = createNew(unitOfWork, tuning);
            } else {
                result = unitOfWork.execute();
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("TX-Required exception: {}; txId:{}", ExceptionUtils.getRootCauseMessage(e), getTxId(tx));
            }
           rethrow(e);
        } finally {
            logger.debug("TX-Required end");
        }
        return result;
    }

    @Override
    public <R> R requiresNew(UnitOfWork<R> unitOfWork, U tuning) {
        T tx = transactions.get();
        if (logger.isDebugEnabled()) {
            logger.debug("TX-RequiresNew begin thId:{}", getTxId(tx));
        }
        R result = null;
        try {
            if (tx == null) {
                result = createNew(unitOfWork, tuning);
            } else {
                try {
                    transactions.remove();
                    result = createNew(unitOfWork, tuning);
                } finally {
                    transactions.set(tx);
                }
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("TX-RequiresNew exception: {}; txId:{}", ExceptionUtils.getRootCauseMessage(e), getTxId(tx));
            }
           rethrow(e);
        } finally {
            logger.debug("TX-RequiresNew end");
        }
        return result;
    }

    @Override
    public <R> R mandatory(UnitOfWork<R> unitOfWork, U tuning) {
        R result = null;
        T tx = transactions.get();
        if (logger.isDebugEnabled()) {
            logger.debug("TX-Mandatory begin thId:{}", getTxId(tx));
        }
        try {
            if (tx == null) {
                throw new IllegalStateException("No active transaction");
            }
            result = unitOfWork.execute();
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("TX-Mandatory exception: {}; txId:{}", ExceptionUtils.getRootCauseMessage(e), getTxId(tx));
            }
           rethrow(e);
        } finally {
            logger.debug("TX-Mandatory end");
        }
        return result;
    }

    @Override
    public <R> R notSupported(UnitOfWork<R> unitOfWork, U tuning) {
        T tx = transactions.get();
        if (logger.isDebugEnabled()) {
            logger.debug("TX-NotSupported begin thId:{}", getTxId(tx));
        }
        R result = null;
        try {
            if (tx == null) {
                result = unitOfWork.execute();
            } else {
                try {
                    transactions.remove();
                    return unitOfWork.execute();
                } finally {
                    transactions.set(tx);
                }
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("TX-NotSupported exception: {}; txId:{}", ExceptionUtils.getRootCauseMessage(e), getTxId(tx));
            }
           rethrow(e);
        } finally {
            logger.debug("TX-NotSupported end");
        }
        return result;
    }

    @Override
    public <R> R supports(UnitOfWork<R> unitOfWork, U tuning) {
        R result = null;
        T tx = transactions.get();
        if (logger.isDebugEnabled()) {
            logger.debug("TX-Supports begin thId:{}", getTxId(tx));
        }
        try {
            result = unitOfWork.execute();
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("TX-Supports exception: {}; txId:{}", ExceptionUtils.getRootCauseMessage(e), getTxId(tx));
            }
           rethrow(e);
        } finally {
            logger.debug("TX-Supports end");
        }
        return result;
    }

    @Override
    public <R> R never(UnitOfWork<R> unitOfWork, U tuning) {
        T tx = transactions.get();
        if (logger.isDebugEnabled()) {
            logger.debug("TX-Never begin thId:{}", getTxId(tx));
        }
        R result = null;
        try {
            if (tx != null) {
                throw new IllegalStateException("Active transaction exists");
            }
            result = unitOfWork.execute();
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("TX-Never exception: {}; txId:{}", ExceptionUtils.getRootCauseMessage(e), getTxId(tx));
            }
           rethrow(e);
        } finally {
            logger.debug("TX-Never ned");
        }
        return result;
    }

    @Override
    public <R> R nested(UnitOfWork<R> unitOfWork, U tuning) {
        //For jdbc based transactions this can be implemented with savepoints (see jooq as example)
        throw new UnsupportedOperationException("Nested transactions is not supported.");
    }

    /**
     * Returns active transaction, trown exception otherwise
     *
     * @return
     */
    public T getTransaction() {
        T tx = transactions.get();
        if (tx == null) {
            throw new IllegalStateException("No active transaction");
        }
        return tx;
    }

    /**
     * Returns active transaction or null
     *
     * @return
     */
    public T getTransactionOrNull() {
        return transactions.get();
    }


    /**
     * Cast a CheckedException as an unchecked one.
     *
     * @param throwable to cast
     * @param <T>       the type of the Throwable
     * @return this method will never return a Throwable instance, it will just throw it.
     * @throws T the throwable as an unchecked throwable
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException rethrow(Throwable throwable) throws T {
        throw (T) throwable; // rely on vacuous cast
    }
}
