package colesico.framework.jjwt;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class JwtConfigPrototype {

    /**
     * Minimum 32 chars
     */
    abstract public String accessSecret();

    abstract public String refreshSecret();

    /**
     * Access accessToken time to live in milliseconds
     * default 15 minutes
     */
    public long accessTtl() {
        return 15 * 60 * 1000;
    }

    /**
     * Refresh accessToken time to live in milliseconds
     * default 7 days
     */
    public long refreshTtl() {
        return 7 * 24 * 60 * 60 * 1000;
    }

    public String accessTokenExpiredResponse() {
        return "AccessTokenExpired";
    }

    public String accessTokenExpiredRedirect() {
        return "/identity/jwt/refresh";
    }
}

