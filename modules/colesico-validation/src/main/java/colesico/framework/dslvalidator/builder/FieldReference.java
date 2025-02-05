package colesico.framework.dslvalidator.builder;

import java.util.function.Function;

/**
 * Field reference
 * @param <V> class containing field
 * @param <N> field type
 */
public interface FieldReference<V,N> {

    /**
     * Validation subject  associated with field.
     * Typically, field name
     */
    String subject();

    /**
     * Field value extractor
     */
    Function<V, N> extractor();
}
