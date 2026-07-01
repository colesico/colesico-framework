package colesico.framework.telehttp.reader;

import colesico.framework.telehttp.TeleHttpReadOptions;
import colesico.framework.telehttp.TeleHttpReader;
import colesico.framework.telehttp.origin.OriginFactory;
import colesico.framework.telehttp.origin.Origin;

/**
 * Basic origin based reader
 *
 * @param <V> type of value to  be read
 * @param <R> read options
 */
abstract public class OriginReader<V, R extends TeleHttpReadOptions> implements TeleHttpReader<V, R> {

    protected final OriginFactory originFactory;

    public OriginReader(OriginFactory originFactory) {
        this.originFactory = originFactory;
    }

    /**
     * Return param string value from origin defined in the context
     */
    protected final Iterable<String> readStrings(R options) {
        Origin origin = originFactory.getOrigin(options.originName());
        return origin.getStrings(options.paramName());
    }

    protected final Iterable<String> readStrings(String originName, String paramName) {
        Origin origin = originFactory.getOrigin(originName);
        return origin.getStrings(paramName);
    }

    protected final String readString(R options) {
        var it = readStrings(options).iterator();
        return it.hasNext() ? it.next() : null;
    }

    protected final String readString(String originName, String paramName) {
        var it = readStrings(originName, paramName).iterator();
        return it.hasNext() ? it.next() : null;
    }

}
