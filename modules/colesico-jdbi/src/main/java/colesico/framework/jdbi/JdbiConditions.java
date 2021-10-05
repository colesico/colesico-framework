package colesico.framework.jdbi;

import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.ConditionContext;

/**
 * JDBI IoC conditions to configure jdbi module
 */
public final class JdbiConditions {

    /**
     * Disable  producing jdbi tx shell as default tx shell
     */
    public static void disableDefaultTransactionalShell() {
        DefaultTransactionalShell.enabled = false;
    }

    /**
     * Disable  producing connection from jdbi tx shell as default connection
     */
    public static void disableDefaultConnection() {
        DefaultConnection.enabled = false;
    }

    /**
     * Disable default jdbi config implementation binding as default jdbi config
     */
    public static void disableDefaultConfig() {
        DefaultConfig.enabled = false;
    }

    public static final class DefaultTransactionalShell implements Condition {

        public static boolean enabled = true;

        @Override
        public boolean isMet(ConditionContext context) {
            return enabled;
        }
    }

    public static final class DefaultConnection implements Condition {

        public static boolean enabled = true;

        @Override
        public boolean isMet(ConditionContext context) {
            return enabled;
        }
    }

    public static final class DefaultConfig implements Condition {

        public static boolean enabled = true;

        @Override
        public boolean isMet(ConditionContext context) {
            return enabled;
        }
    }
}
