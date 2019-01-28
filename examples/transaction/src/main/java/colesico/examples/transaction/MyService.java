package colesico.examples.transaction;

import colesico.framework.service.Service;
import colesico.framework.transaction.TransactionPropagation;
import colesico.framework.transaction.Transactional;
import colesico.framework.transaction.TransactionalShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Service
public class MyService {

    Logger log = LoggerFactory.getLogger(MyService.class);

    private final TransactionalShell<Object> customTxShell;

    public MyService(@Named("custom") TransactionalShell customTxShell) {
        this.customTxShell = customTxShell;
    }

    /**
     * Declarative  transaction control style with default transactional shell
     *
     * @param value
     * @return
     */
    @Transactional(propagation = TransactionPropagation.REQUIRES_NEW)
    public Boolean save(String value) {
        log.info("Value=" + value);
        return Boolean.TRUE;
    }

    /**
     * Declarative  transaction control style with custom transactional shell
     *
     * @param value
     * @return
     */
    @Transactional(shell = "custom")
    public Boolean update(String value) {
        log.info("Value=" + value);
        return Boolean.TRUE;
    }

    /**
     * @param value
     * @return
     */
    @Transactional(shell = "alternative")
    public Boolean update2(String value) {
        log.info("Value=" + value);
        return Boolean.TRUE;
    }

    // Mixed programmatic  and declarative transaction control style
    @Transactional(shell = "custom")
    public Boolean create(String value) {
        return customTxShell.requiresNew(() -> {
            log.info("Value=" + value);
            return Boolean.FALSE;
        });
    }

    /**
     * Programmatic transaction control style
     *
     * @param value
     * @return
     */
    public Boolean delete(String value) {
        return customTxShell.required(() -> {
            log.info("Value=" + value);
            return Boolean.FALSE;
        });
    }
}
