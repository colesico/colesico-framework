package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

import java.util.HashMap;

public class JwtSubjectAuthenticator
        implements Authenticator<JwtSubjectRequest, JwtChallenge> {

    private final JwtTokenUtils tokenUtils;

    public JwtSubjectAuthenticator(JwtTokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public AuthenticationResult<JwtChallenge> authenticate(JwtSubjectRequest request) {

        String accessToken = tokenUtils.generateAccessToken(request.subject(), request.claims());
        String refreshToken = tokenUtils.generateRefreshToken(request.subject(), new HashMap<>());

        return AuthenticationResult.stage(new JwtTokensChallenge(accessToken, refreshToken));
    }

}
