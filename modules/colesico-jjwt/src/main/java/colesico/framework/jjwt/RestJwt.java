package colesico.framework.jjwt;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.security.authentication.AuthenticatorRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class RestJwt extends JwtTokenSource {

    protected static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    protected static final String AUTHORIZATION_HEADER = "Authorization";

    private static final Gson gson = new GsonBuilder().create();

    public RestJwt(JwtConfigPrototype config, Provider<HttpContext> httpContext) {
        super(config, httpContext);
    }

    @Override
    public JwtRequest request() {
        String authHeader = this.httpContext.get().request().headers().get(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authHeader)) return null;

        Matcher m = BEARER_PATTERN.matcher(authHeader.trim());
        if (m.matches()) {
            return new JwtRequest(m.group(1),
                    AuthenticatorRequest.sourceClaims(this.getClass()));
        }

        return null;
    }


    @Override
    public void onStage(JwtChallenge challenge) {
        var response = httpContext.get().response();

        switch (challenge) {

            case JwtTokensChallenge jtc -> {
                JsonObject resp = new JsonObject();
                resp.addProperty("access_token", jtc.accessToken());
                resp.addProperty("refresh_token", jtc.refreshToken());

                response.setStatus(401)
                        .setContentType("application/json")
                        .send(gson.toJson(resp));
            }

            case JwtRefreshChallenge jrc -> {
                JsonObject resp = new JsonObject();
                resp.addProperty("refresh", true);

                response.setStatus(401)
                        .setContentType("application/json")
                        .send(gson.toJson(resp));
            }

            default -> throw new IllegalStateException("Unexpected jwt challenge: " + challenge);
        }


    }

}
