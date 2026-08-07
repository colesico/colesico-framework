package colesico.framework.security.authentication;

/**
 * Represents a transport-level participant in the authentication process.
 * <p>
 * An AuthenticationSource acts as a bridge between the security framework and specific
 * communication protocols such as HTTP, gRPC, or Message Queues. It is responsible
 * for extracting credentials and handling protocol-specific responses.
 */
public interface AuthenticationSource<
        R extends AuthenticationRequest,
        C extends AuthenticationChallenge>
        extends AuthenticationCallback<R, C> {

    /**
     * Extracts an {@link AuthenticationRequest} from the underlying transport.
     *
     * @return the request containing credentials, or {@code null} if no credentials
     * are present in this source.
     */
    R request();
}
