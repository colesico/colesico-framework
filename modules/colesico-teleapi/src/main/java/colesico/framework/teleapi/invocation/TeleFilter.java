package colesico.framework.teleapi.invocation;

//TODO: implement telefilter mecanic
public interface TeleFilter<X> {
    void doFilter(X protocolExchange);
}
