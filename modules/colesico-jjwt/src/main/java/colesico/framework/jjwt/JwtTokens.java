package colesico.framework.jjwt;

public record JwtTokens(
        String accessToken,
        String refreshToken) {
}
