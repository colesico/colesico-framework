package colesico.framework.security.authentication;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current peer holder
 */
public interface PeerContext {

    Key<Peers> SCOPE_KEY = new TypeKey<>(Peers.class);

    /**
     * Returns {@link AuthenticationPeer} bound to current scope  (thread, request, etc)
     */
    Iterable<AuthenticationPeer> peers();

    void setPeers(Iterable<AuthenticationPeer> peers);

    /**
     * Remove peer bound to current scope
     */
    void clear();

    record Peers(Iterable<AuthenticationPeer> items){

    }
}
