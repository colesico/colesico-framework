package colesico.framework.telehttp;

import java.util.Collection;

/**
 * Basic origin based reader
 *
 * @param <V> type of value to  be read
 * @param <C> reading context
 */
abstract public class OriginTeleReader<V, C extends HttpTRContext> implements HttpTeleReader<V, C> {

    protected final OriginFactory originFactory;

    public OriginTeleReader(OriginFactory originFactory) {
        this.originFactory = originFactory;
    }

    /**
     * Return param string value from origin defined in the context
     */
    protected final Collection<String> readStrings(C context) {
        Origin origin = originFactory.getOrigin(context.originName());
        return origin.getStrings(context.paramName());
    }

    protected final Collection<String> readStrings(String originName, String paramName) {
        Origin origin = originFactory.getOrigin(originName);
        return origin.getStrings(paramName);
    }

    protected final String readString(C context) {
        return readStrings(context).stream().findFirst().orElse(null);
    }

    protected final String readString(String originName, String paramName) {
        return readStrings(originName, paramName).stream().findFirst().orElse(null);
    }

}
