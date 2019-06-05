package colesico.framework.asyncjob;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.transaction.TransactionalShell;

import java.sql.Connection;
import java.time.Duration;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class JobServiceConfig {

    public static final Long DEFAULT_IDLE_TIMEOUT = Duration.ofSeconds(20).toMillis();
    public static final Long DEFAULT_CAPTURE_TIMEOUT = Duration.ofSeconds(60).toMillis();

    /**
     * Return transactional connection to database
     *
     * @return
     */
    abstract public Connection getConnection();

    abstract public TransactionalShell getTransactionalShell();

    /**
     * Return number of workers to process queues
     *
     * @return
     */
    public int getWorkersNum() {
        return 2;
    }

    /**
     * The delay interval before re-checking the appearance of jobs in the queues in milliseconds
     *
     * @return
     */
    public long getWorkerIdleTimeoutMs() {
        return DEFAULT_IDLE_TIMEOUT;
    }

    public long getQueueCaptureTimeoutMs() {
        return DEFAULT_CAPTURE_TIMEOUT;
    }

}
