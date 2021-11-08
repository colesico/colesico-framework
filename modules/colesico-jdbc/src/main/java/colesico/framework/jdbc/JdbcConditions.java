package colesico.framework.jdbc;

import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.ConditionContext;

/**
 * JDBI IoC conditions to configure module
 */
public final class JdbcConditions {

    /**
     * Disable jdbc tx shell producing as default tx shell
     */
    public static void disableDefaultTransactionalShell() {
        DefaultTransactionalShell.enabled = false;
    }

    /**
     * Disable connection producing from default tx shall as jdbc tx shell
     */
    public static void disableDefaultConnection() {
        DefaultConnection.enabled = false;
    }

    public static final class DefaultTransactionalShell implements Condition {

        private static boolean enabled = true;

        @Override
        public boolean isMet(ConditionContext context) {
            return enabled;
        }
    }

    public static final class DefaultConnection implements Condition {

        private static boolean enabled = true;

        @Override
        public boolean isMet(ConditionContext context) {
            return enabled;
        }
    }
}
