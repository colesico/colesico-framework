package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.AuthenticatorOutcome;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.internal.BasicAuthProducer;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * Simple basic authenticator
 *
 * @see BasicAuthProducer
 */
public class BasicAuthenticator
        implements Authenticator<BasicRequest, BasicCallback> {

    /**
     * Authenticator config
     */
    protected final BasicAuthConfigPrototype config;

    /**
     * Accounts storage
     */
    protected final BasicAccountStorage accounts;

    /**
     * Authenticated identities
     */
    protected final Map<Object, Identity> authenticated;

    @Inject
    public BasicAuthenticator(BasicAuthConfigPrototype config, BasicAccountStorage accounts) {
        this.config = config;
        this.accounts = accounts;

        authenticated = Collections.synchronizedMap(
                new LinkedHashMap<>(100, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry eldest) {
                        return size() > config.maxAuthenticated();
                    }
                }
        );
    }

    protected Identity performAuth(BasicRequest request) {
        String passwordHex;
        try {
            MessageDigest digest = MessageDigest.getInstance(config.passwordDigest());
            byte[] passwordHash = digest.digest(
                    request.password().getBytes(StandardCharsets.UTF_8));
            passwordHex = HexFormat.of().formatHex(passwordHash);
        } catch (Exception ex) {
            throw new SecurityException(ex);
        }

        BasicAccountStorage.Account account = accounts.findAccount(request.login(), passwordHex);
        if (account == null) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>(request.claims());
        claims.put(Identity.AUTHENTICATOR_CLAIM, BasicAuthenticator.class);
        claims.put(Identity.ROLES_CLAIM, account.roles());
        return Identity.Default.of(request.login(), claims);
    }

    @Override
    public AuthenticatorOutcome authenticate(BasicRequest request, BasicCallback callback) {
        Optional<BasicCallback> cb = Optional.ofNullable(callback);

        if (request.isEmpty()) {
            var realm = config.realm();
            if (realm != null) {
                cb.ifPresent(c -> c.onChallenge(realm));
                return AuthenticatorOutcome.stage();
            } else {
                return AuthenticatorOutcome.skip("No realm provided");
            }
        }

        var login = request.login();
        var identity = authenticated.get(login);
        if (identity != null) {
            if (cb.isPresent()) {
                cb.get().onLogin(identity);
            }
            return AuthenticatorOutcome.success(identity);
        }

        identity = performAuth(request);
        if (identity != null) {
            authenticated.put(login, identity);
            if (cb.isPresent()) {
                cb.get().onLogin(identity);
            }
            return AuthenticatorOutcome.success(identity);
        }

        return AuthenticatorOutcome.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity identity, BasicCallback callback) {
        if (identity != null) {
            authenticated.remove(identity.id());
            if (callback != null) {
                callback.onLogout(identity);
            }
        }
    }

}
