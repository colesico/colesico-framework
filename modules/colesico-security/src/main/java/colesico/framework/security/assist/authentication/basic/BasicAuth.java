package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.Authentication;
import colesico.framework.security.authentication.AuthenticationOutcome;
import colesico.framework.security.authentication.LogoutMessage;
import colesico.framework.security.internal.BasicAuthProducer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * Simple basic authentication flow
 *
 * @see BasicAuthProducer
 */
@Singleton
public class BasicAuth implements Authentication<BasicMessage, LogoutMessage> {

    /**
     * Authenticator config
     */
    protected final BasicConfigPrototype config;

    /**
     * Accounts storage
     */
    protected final BasicAccounts accounts;

    protected final BasicSource source;

    /**
     * Authenticated identities
     */
    protected final Map<Object, Identity<?>> authenticated;

    @Inject
    public BasicAuth(BasicConfigPrototype config, BasicSource source, BasicAccounts accounts) {
        this.config = config;
        this.accounts = accounts;
        this.source = source;

        authenticated = Collections.synchronizedMap(
                new LinkedHashMap<>(100, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry eldest) {
                        return size() > config.maxAuthenticated();
                    }
                }
        );
    }

    protected Identity<?> performAuth(BasicMessage request) {
        String passwordHex;
        try {
            MessageDigest digest = MessageDigest.getInstance(config.passwordDigest());
            byte[] passwordHash = digest.digest(
                    request.password().getBytes(StandardCharsets.UTF_8));
            passwordHex = HexFormat.of().formatHex(passwordHash);
        } catch (Exception ex) {
            throw new SecurityException(ex);
        }

        BasicAccounts.Account account = accounts.findAccount(request.login(), passwordHex);
        if (account == null) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(Identity.AUTHENTICATION_CLAIM, BasicAuth.class);
        claims.put(Identity.ROLES_CLAIM, account.roles());
        return Identity.Default.of(request.login(), claims);
    }

    @Override
    public AuthenticationOutcome authenticate(BasicMessage message) {

        if (message == null) {
            message = source.credentials();
        }

        if (message == null) {
            var realm = config.realm();
            if (realm != null) {
                source.challenge(realm);
                return AuthenticationOutcome.stage();
            } else {
                return AuthenticationOutcome.skip("No realm provided");
            }
        }

        var login = message.login();
        var identity = authenticated.get(login);
        if (identity != null) {
            return AuthenticationOutcome.success(identity);
        }

        identity = performAuth(message);
        if (identity != null) {
            authenticated.put(login, identity);
            return AuthenticationOutcome.success(identity);
        }

        return AuthenticationOutcome.failure("Invalid credentials");
    }

    @Override
    public void logout(LogoutMessage message) {
        authenticated.remove(message.identity().id());
        source.logout(message.identity());
    }

}
