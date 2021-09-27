package colesico.framework.hikaricp;

import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.ConditionContext;

/**
 * HikariCP IoC conditions to configure module
 */
public final class HikariCPConditions {

    /**
     * Disable HikariDataSource producing as default DataSource
     */
    public static void disableDefaultDataSource() {
        DefaultDataSource.enabled = false;
    }

    public static final class DefaultDataSource implements Condition {

        public static boolean enabled = true;

        @Override
        public boolean isMet(ConditionContext context) {
            return enabled;
        }
    }
}
