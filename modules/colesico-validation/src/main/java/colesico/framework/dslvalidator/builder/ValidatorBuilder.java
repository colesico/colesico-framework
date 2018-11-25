package colesico.framework.dslvalidator.builder;


import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.commands.*;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.translation.Translatable;

import java.util.function.Predicate;

abstract public class ValidatorBuilder extends FlowControlBuilder {

    protected final ValidatorMessages vrMessages;

    public ValidatorBuilder(ValidatorMessages vrMessages) {
        this.vrMessages = vrMessages;
    }

    /**
     * Adds required verifier to local chain
     */
    protected CommandToken required() {
        return () -> add(new RequiredVerifier(vrMessages));
    }

    /**
     * Adds predicate verifier
     *
     * @param predicate
     * @param errorCode
     * @param errorMessage
     * @param messageParam
     * @param <T>
     */
    protected <T> CommandToken predicate(final Predicate<ValidationContext<T>> predicate,
                                         final String errorCode,
                                         final Translatable errorMessage,
                                         final Object... messageParam) {
        return () -> add(new PredicateVerifier(predicate, errorCode, errorMessage, messageParam));
    }

    /**
     * Adds regexp verifier
     *
     * @param pattern
     * @param errorCode
     * @param errorMessage
     * @param messageParam
     */
    protected final CommandToken regexp(final String pattern,
                                        final String errorCode,
                                        final Translatable errorMessage,
                                        final Object... messageParam) {

        return () -> add(new RegexpVerifier(pattern, errorCode, errorMessage, messageParam));
    }

    /**
     * Adds string length verifier
     *
     * @param min
     * @param max
     */
    protected final CommandToken length(final Integer min,
                                        final Integer max) {

        return () -> add(new LengthVerifier(min, max, vrMessages));
    }

    protected final CommandToken interval(final Number min,
                                          final Number max,
                                          final boolean includeEndpoints) {

        return () -> add(new IntervalVerifier(min, max, includeEndpoints, vrMessages));
    }

    protected final CommandToken dateFormat(final String format) {
        return () -> add(new DateFormatVerifier(format, vrMessages));
    }

}
