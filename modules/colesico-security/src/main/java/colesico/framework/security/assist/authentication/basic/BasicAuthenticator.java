package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationOutcome;
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
public class BasicAuthenticator implements
        Authenticator<BasicAuthenticationRequest> {

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

    protected Identity performAuth(BasicAuthenticationRequest request) {
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
    public AuthenticationOutcome authenticate(BasicAuthenticationRequest request) {

        if (request.isEmpty()) {
            var challenge = config.challenge();
            if (challenge != null) {
                return challenge;
            } else {
                return AuthenticationOutcome.skip("No challenge required");
            }
        }

        var login = request.login();
        var identity = authenticated.get(login);
        if (identity != null) {
            return AuthenticationOutcome.success(identity);
        }

        identity = performAuth(request);
        if (identity != null) {
            authenticated.put(login, identity);
            return AuthenticationOutcome.success(identity);
        }

        return AuthenticationOutcome.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity identity) {
        if (identity != null) {
            authenticated.remove(identity.id());
        }
    }

}
