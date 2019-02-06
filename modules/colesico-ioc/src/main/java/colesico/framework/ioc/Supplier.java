package colesico.framework.ioc;


/**
 * Basic supplier interface.
 * Supplier is used to obtain instances from IoC container with support of message pass to the factory that creates that instance
 */
public interface Supplier<T> {

    String GET_METHOD = "get";
    String MESSAGE_PARAM = "message";

    /**
     * Should returns instance of T
     *
     * @see Message
     */
    T get(Object message);
}
