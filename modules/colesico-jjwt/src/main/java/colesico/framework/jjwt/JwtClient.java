package colesico.framework.jjwt;

public interface JwtClient {

    void populateTokens(String accessToken, String refreshToken);

    String readAccessToken();

    String readRefreshToken();

    void askRefreshToken();

    void clearTokens();
}