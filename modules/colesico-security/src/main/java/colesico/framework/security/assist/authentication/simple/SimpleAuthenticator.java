package colesico.framework.security.assist.authentication.simple;

import colesico.framework.security.Identity;
import colesico.framework.security.assist.authentication.BasicAuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * Simple authenticator
 *
 * @see colesico.framework.security.internal.SimpleAuthProducer
 */
public class SimpleAuthenticator implements
        Authenticator<BasicAuthenticationRequest> {

    /**
     * Authenticator config
     */
    protected final SimpleAuthConfigPrototype config;

    /**
     * Accounts storage
     */
    protected final SimpleAccountStorage accounts;

    /**
     * Authenticated identities
     */
    protected final Map<Object, Identity<?>> authenticated;

    public SimpleAuthenticator(SimpleAuthConfigPrototype config, SimpleAccountStorage accounts) {
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

    protected Identity<?> authenticate(BasicAuthenticationRequest request) {
        String passwordHash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    request.password().getBytes(StandardCharsets.UTF_8));
            passwordHash = HexFormat.of().formatHex(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        SimpleAccountStorage.Account account = accounts.findAccount(request.login(), passwordHash);
        if (account == null) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>(request.claims());
        claims.put(Identity.AUTHENTICATOR_CLAIM, SimpleAuthenticator.class);
        claims.put(Identity.ROLES_CLAIM, account.roles());
        return Identity.Default.of(request.login(), claims);
    }

    @Override
    public boolean supports(BasicAuthenticationRequest request) {
        return true;
    }

    @Override
    public AuthenticationResult login(BasicAuthenticationRequest request) {
        var login = request.login();

        if (login == null) {
            return AuthenticationResult.continuation(config.challenge());
        }

        var identity = authenticated.get(login);
        if (identity != null) {
            return AuthenticationResult.success(identity);
        }

        identity = authenticate(request);
        if (identity != null) {
            authenticated.put(login, identity);
            return AuthenticationResult.success(identity);
        }

        return AuthenticationResult.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity<?> identity) {
        if (identity != null) {
            authenticated.remove(identity.id());
        }
    }

}
