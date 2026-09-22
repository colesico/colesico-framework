package colesico.framework.telehttp.result;

public interface ValueResult<V> extends HttpResult {
    V value();
}
