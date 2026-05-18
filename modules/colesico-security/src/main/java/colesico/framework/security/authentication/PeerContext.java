package colesico.framework.security.authentication;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.security.Identity;

import java.util.Collection;

/**
 * Current peer holder
 */
public interface PeerContext {

    Key<AuthenticationPeer> SCOPE_KEY = new TypeKey<>(AuthenticationPeer.class);

    /**
     * Returns {@link AuthenticationPeer} bound to current scope  (thread, request, etc)
     */
    Iterable<AuthenticationPeer> peers();

    void setPeers(Iterable<AuthenticationPeer> peers);

    /**
     * Remove peer bound to current scope
     */
    void clear();

}
