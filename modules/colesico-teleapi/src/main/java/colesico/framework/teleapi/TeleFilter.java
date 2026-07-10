package colesico.framework.teleapi;

//TODO: implement tele-filter mechanic

public interface TeleFilter<X> {
    void doFilter(X protocolExchange);
}
