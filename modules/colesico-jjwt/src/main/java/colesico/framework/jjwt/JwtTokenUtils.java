package colesico.framework.jjwt;

import colesico.framework.security.SecurityException;
import colesico.framework.security.authentication.AuthenticationOutcome;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.inject.Singleton;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Singleton
public class JwtTokenUtils {


    public static final String TOKEN_TYPE_CLAIM = "type";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    private final long accessTtl;
    private final long refreshTtl;


    public JwtTokenUtils(JwtConfigPrototype config) {
        this.accessKey = Keys.hmacShaKeyFor(config.accessSecret().getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(config.refreshSecret().getBytes(StandardCharsets.UTF_8));
        this.accessTtl = config.accessTtl();
        this.refreshTtl = config.refreshTtl();
    }

    public String createAccessToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTtl))
                .signWith(accessKey)
                .compact();
    }

    public String createRefreshToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .issuedAt(new Date())
                .id(UUID.randomUUID().toString())
                .expiration(new Date(System.currentTimeMillis() + refreshTtl))
                .signWith(refreshKey)
                .compact();
    }

    private Claims parseToken(String token, SecretKey key) throws ExpiredJwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims parseAccessToken(String tokenStr) {
        var claims = parseToken(tokenStr, accessKey);
        var tokenType = claims.get(JwtTokenUtils.TOKEN_TYPE_CLAIM);
        if (!ACCESS_TOKEN_TYPE.equals(tokenType)) {
            throw new SecurityException("Invalid access token type");
        }
        return claims;
    }

    public Claims parseRefreshToken(String token) {
        var claims = parseToken(token, refreshKey);
        var tokenType = claims.get(JwtTokenUtils.TOKEN_TYPE_CLAIM);
        if (!REFRESH_TOKEN_TYPE.equals(tokenType)) {
            throw new SecurityException("Invalid refresh token type");
        }
        return claims;
    }
}
