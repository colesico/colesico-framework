package colesico.framework.asyncjob;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

/**
 * Queue configuration
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class JobQueueConfigPrototype {
    /**
     * Database table name for storing jobs
     *
     * @return
     */
    abstract public String getTableName();

    abstract public JobConsumer getJobConsumer();

    abstract public Class<?> getPayloadType();

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "{table=" + getTableName() + ", payloadType=" + getPayloadType() + '}';
    }

}
