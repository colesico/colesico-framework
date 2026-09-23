package colesico.framework.telehttp.result;

public interface ValueHttpResult<V> extends HttpResult {
    V value();
}
