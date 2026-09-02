package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.IssuedIdentity;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.AuthenticationOutcome;
import colesico.framework.security.authentication.LogoutMessage;
import colesico.framework.security.internal.BasicAuthProducer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * Simple basic authenticator
 *
 * @see BasicAuthProducer
 */
@Singleton
public class SimpleBasic implements Authenticator<BasicMessage, LogoutMessage> {

    /**
     * Authentication flow config
     */
    protected final BasicConfigPrototype config;

    /**
     * Accounts storage
     */
    protected final BasicAccounts accounts;

    /**
     * Client API
     */
    protected final BasicClient client;

    /**
     * Authenticated identities
     */
    protected final Map<Object, Identity> authenticated;

    @Inject
    public SimpleBasic(BasicConfigPrototype config,
                       BasicClient client,
                       BasicAccounts accounts) {

        this.config = config;
        this.accounts = accounts;
        this.client = client;

        authenticated = Collections.synchronizedMap(
                new LinkedHashMap<>(100, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry eldest) {
                        return size() > config.maxAuthenticated();
                    }
                }
        );
    }

    protected String encodePassword(byte[] passwordHash) {
        return HexFormat.of().formatHex(passwordHash);
    }

    protected Identity performAuth(BasicMessage request) {
        String passwordEnc;
        try {
            MessageDigest digest = MessageDigest.getInstance(config.passwordDigest());
            byte[] passwordHash = digest.digest(
                    request.password().getBytes(StandardCharsets.UTF_8));
            passwordEnc = encodePassword(passwordHash);
        } catch (Exception ex) {
            throw new SecurityException(ex);
        }

        BasicAccounts.Account account = accounts.findAccount(request.login(), passwordEnc);
        if (account == null) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(Identity.ROLES_CLAIM, account.roles());
        return IssuedIdentity.of(this, request.login(), claims);
    }

    @Override
    public AuthenticationOutcome authenticate(BasicMessage message) {

        if (message == null) {
            message = client.message();
        }

        if (message == null) {
            var realm = config.realm();
            if (realm != null) {
                client.challenge(realm);
                return AuthenticationOutcome.stage("CredentialsRequired");
            } else {
                return AuthenticationOutcome.bypass("NoRealmProvided");
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

        return AuthenticationOutcome.failure("InvalidCredentials");
    }

    @Override
    public void logout(LogoutMessage message) {
        authenticated.remove(message.identity().id());
        client.logout(message.identity());
    }

}
