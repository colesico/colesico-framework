package colesico.framework.dslvalidator.builder;


import colesico.framework.dslvalidator.Command;
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
     * Verify value exists (string is not blank, collection is not empty, etc)
     */
    protected <V> Command<V> required() {
        return new RequiredVerifier<>(vrMessages);
    }

    /**
     * Adds predicate verifier
     *
     * @param predicate
     * @param errorCode
     * @param errorMessage
     * @param messageParam
     */
    protected <V> Command<V> predicate(final Predicate<ValidationContext<V>> predicate,
                                    final String errorCode,
                                    final Translatable errorMessage,
                                    final Object... messageParam) {
        return new PredicateVerifier<>(predicate, errorCode, errorMessage, messageParam);
    }

    /**
     * Verify by regexp match
     *
     * @param pattern
     * @param errorCode
     * @param errorMessage
     * @param messageParam
     */
    protected final Command<String> regexp(final String pattern,
                                   final String errorCode,
                                   final Translatable errorMessage,
                                   final Object... messageParam) {

        return new RegexpVerifier(pattern, errorCode, errorMessage, messageParam);
    }

    /**
     * Verify string length
     *
     * @param min
     * @param max
     */
    protected final Command<String> length(final Integer min,
                                   final Integer max) {

        return new LengthVerifier(min, max, vrMessages);
    }

    /**
     * Verify number range
     * @param min
     * @param max
     * @param includeEndpoints
     * @return
     */
    protected final <V extends Number> Command<V> interval(final Number min,
                                     final Number max,
                                     final boolean includeEndpoints) {

        return new IntervalVerifier(min, max, includeEndpoints, vrMessages);
    }

    /**
     * Verify collection size range
     * @param min
     * @param max
     * @param includeEndpoints
     * @return
     */
    protected final <V> Command<V> size(final Number min,
                                 final Number max,
                                 final boolean includeEndpoints) {

        return new SizeVerifier(min, max, includeEndpoints, vrMessages);
    }

    /**
     * Verify date format
     * @param format
     * @return
     */
    protected final Command<String> dateFormat(final String format) {
        return new DateFormatVerifier(format, vrMessages);
    }

}
