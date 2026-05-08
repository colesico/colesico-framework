package colesico.framework.security.authentication;

public interface AuthenticationContextReader {
    /**
     * Read {@link AuthenticationContext} from source.
     * Override this method to fine-grained read control: check validity,
     * enrich with extra data, e.t.c.
     */
    AuthenticationContext read();
}
