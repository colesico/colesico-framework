package colesico.framework.http;

public interface CookieFactory {
    HttpCookie create(String name, String value);
}
