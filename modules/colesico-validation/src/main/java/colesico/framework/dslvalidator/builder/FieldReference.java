package colesico.framework.dslvalidator.builder;

import java.util.function.Function;

/**
 * Validated bean field reference to use in validator builder
 *
 * @param subject Validation subject  associated with field. Typically, field name
 * @param mapper  Field value mapper
 * @param <V>     class containing field
 * @param <N>     field type
 */
public record FieldReference<V, N>(String subject, Function<V, N> mapper) {

}
