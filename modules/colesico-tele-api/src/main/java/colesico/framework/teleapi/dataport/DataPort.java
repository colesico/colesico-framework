package colesico.framework.teleapi.dataport;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import java.lang.reflect.Type;

/**
 * Gateway for data exchange with a remote source (via HTTP, gRPC, Kafka, etc.).
 * A data port always has a protocol-specific implementation.
 * <p>
 * DataPort dispatches read/write requests to the appropriate {@link TeleReader} or {@link TeleWriter}
 * based on the value type. It may also implement cross-cutting concerns such as security,
 * logging, or encryption before/after delegation.
 * <p>
 * Key responsibilities:
 * <ul>
 *     <li><b>Logical options</b> ({@link ReadOptions}/{@link WriteOptions}) are implementation-dependent
 *     types specifying <i>how</i> and <i>what</i> to read or write (e.g., cookie name, specific headers).</li>
 *     <li><b>Metadata</b> is an arbitrary object bound to the type definition (e.g., default value, field annotations).</li>
 *     <li><b>TeleReader/TeleWriter</b> perform actual value transformations and mappings. They obtain raw protocol
 *     objects (e.g., {@code HttpServletRequest}) from the execution context (e.g., {@code ThreadScope}).</li>
 * </ul>
 *
 * @param <R> read options type (extends {@link ReadOptions})
 * @param <W> write options type (extends {@link WriteOptions})
 * @see TeleReader
 * @see TeleWriter
 */
public interface DataPort<R extends ReadOptions, W extends WriteOptions> {

    /**
     * Operation name for read, used in logging/interception/code generation.
     */
    String READ_METHOD = "read";

    /**
     * Operation name for write, used in logging/interception/code generation.
     */
    String WRITE_METHOD = "write";

    /**
     * Key to store DataPort instance in a scope (e.g., ThreadScope).
     */
    Key<DataPort> SCOPE_KEY = new TypeKey<>(DataPort.class);

    /**
     * Reads a value using the given read options.
     */
    <V> V read(R options);

    /**
     * Reads the value, internally creating the appropriate {@link ReadOptions},
     * and delegates the call to {@link #read(ReadOptions)}.
     */
    <V> V read(Type baseType, Object metadata);

    /**
     * Reads the value by baseType without additional metadata.
     */
    default <V> V read(Type baseType) {
        return read(baseType, null);
    }

    /**
     * Writes a value using the given write options.
     */
    <V> void write(V value, W options);

    /**
     * Writes the value internally creating the appropriate {@link WriteOptions}
     * and delegates the call to {@link #write(Object, WriteOptions)}.
     */
    <V> void write(V value, Type baseType, Object metadata);

    /**
     * Writes the value by baseType without additional metadata.
     */
    default <V> void write(V value, Type baseType) {
        write(value, baseType, null);
    }
}