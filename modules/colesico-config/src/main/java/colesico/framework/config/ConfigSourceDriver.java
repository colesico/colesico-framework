package colesico.framework.config;

/**
 * Configuration source driver.
 * Supplies an unified api to obtain configuration values from any source (file, database, http resource, e.t.c.)
 */
public interface ConfigSourceDriver {
    String GET_VALUE_METHD = "getValue";

    /**
     * Get connection to configuration source
     *
     * @param uri
     * @return
     */
    Connection connect(String uri);

    interface Connection {
        String CONNECT_METHD = "connect";
        String CLOSE_METHD = "close";

        /**
         * Returns a configuration value by query
         *
         * @param valueType
         * @param queryText
         * @param <T>
         * @return
         */
        <T> T getValue(Connection connection, Class<T> valueType, String queryText);

        void close();
    }
}
