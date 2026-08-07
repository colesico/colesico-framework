package colesico.framework.jjwt;

public record JwtTokensChallenge(
        String accessToken,
        String refreshToken
) implements JwtChallenge {
}