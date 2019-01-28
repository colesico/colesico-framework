package colesico.framework.transaction;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

    /**
     * Transaction propagation type
     *
     * @return
     */
    TransactionPropagation propagation() default TransactionPropagation.REQUIRED;

    /**
     * Named qualifier value of TransactionalShell field
     *
     * @return
     * @see Named
     */
    String shell() default "";

}
