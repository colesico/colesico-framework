package colesico.framework.validation;

public interface Validator<V> {

    /**
     * Validate a value.
     *
     * @param value
     * @return null if ok, ValidationIssue otherwise
     */
    ValidationIssue validate(V value);

    /**
     * Validates a value
     * If any erors throw Validation exception
     *
     * @param value
     * @throws ValidationException
     */
    void accept(V value) throws ValidationException;
}
