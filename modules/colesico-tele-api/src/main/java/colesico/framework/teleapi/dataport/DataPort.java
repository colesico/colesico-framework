package colesico.framework.teleapi.dataport;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Gateway for data exchange with the remote source (via HTTP, gRPC, Kafka, etc.).
 * Data port has always protocol specific implementation.
 * <p>
 * DataPort dispatches read/write requests to the appropriate {@link TeleReader} or {@link TeleWriter}
 * based on the value type. It may also implement cross-cutting concerns such as security,
 * logging, or encryption before/after delegation.
 * <p>
 * Key responsibilities:
 * <ul>
 *     <li><b>Logical options</b> ({@link ReadOptions}/{@link WriteOptions}) specify <i>what</i> to
 *     read or write (e.g., cookie name).</li>
 *     <li><b>Attachment</b> is an arbitrary message for the reader/writer (correlation ID, hint).</li>
 *     <li><b>TeleReader/TeleWriter</b> perform actual values transformation/mappings. They obtain raw protocol
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
     *
     * @param baseType base type of the value to read
     * @param options  logical read options
     * @param <V>      value type
     * @return the deserialized value
     */
    <V> V read(Class<V> baseType, R options);

    /**
     * Reads a value using default read options.
     */
    <V> V read(Class<V> baseType);

    /**
     * Reads a value using an attachment object.
     * The attachment is embedded to concrete read options (e.g., via {@link ReadOptions#attachment()})
     * and then delegated to {@link #read(Class, ReadOptions)}.
     *
     * @param attachment a message object for the reader
     */
    <V> V read(Class<V> baseType, Object attachment);

    /**
     * Writes a value using the given write options.
     *
     * @param value    value to write
     * @param baseType value base type
     * @param options  write options
     * @param <V>      value type
     */
    <V> void write(V value, Class<V> baseType, W options);

    /**
     * Writes a value using default write options.
     */
    <V> void write(V value, Class<V> baseType);

    /**
     * Writes a value using an attachment object.
     * The attachment is embedded to concrete write options and delegated to
     * {@link #write(Object, Class, WriteOptions)}.
     *
     * @param attachment a message object for the writer (not raw protocol)
     */
    <V> void write(V value, Class<V> baseType, Object attachment);
}