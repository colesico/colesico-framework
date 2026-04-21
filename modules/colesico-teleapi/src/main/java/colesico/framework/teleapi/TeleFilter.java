package colesico.framework.teleapi;

//TODO: implement telefilter mecanic
public interface TeleFilter<X> {
    void doFilter(X protocolExchange);
}
