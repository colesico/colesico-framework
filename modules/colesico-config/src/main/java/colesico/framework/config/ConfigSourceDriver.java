package colesico.framework.config;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Configuration source driver.
 * Supplies an unified api to obtain configuration values from any source (file, database, http resource, e.t.c.)
 */
public interface ConfigSourceDriver {
    String CONNECT_METHOD = "connect";

    /**
     * Get connection to configuration source
     *
     * @return
     */
    Connection connect(Map<String, String> params);

    interface Connection {
        String GET_VALUE_METHOD = "getValue";
        String CLOSE_METHD = "close";

        /**
         * Returns a configuration value by query
         *
         * @param valueType
         * @param queryText
         * @param <T>
         * @return
         */
        <T> T getValue(Type valueType, String queryText, T defaultValue);

        void close();
    }
}
