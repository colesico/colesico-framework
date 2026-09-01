package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple basic authentication peer.
 * Allow to authenticate single user per scope  (default - singleton)
 * Put this source to appropriate scope to support multi user authentication.
 */
@Singleton
public class DefaultClient implements BasicClient {

    protected static final Logger log = LoggerFactory.getLogger(DefaultClient.class);
    protected final AtomicReference<BasicMessage> message = new AtomicReference<>();

    /**
     * Credentials to perform authentication
     */
    public void setCredentials(String login, String password) {
        message.set(new BasicMessage(login, password));
    }

    @Override
    public BasicMessage message() {
        return message.get();
    }

    @Override
    public void challenge(String realm) {
    }

    @Override
    public void logout(Identity identity) {
        message.set(null);
    }
}