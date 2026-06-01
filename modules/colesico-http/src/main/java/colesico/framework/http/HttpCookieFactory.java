package colesico.framework.http;

public interface HttpCookieFactory {
    HttpCookie create(String name, String value);
}
