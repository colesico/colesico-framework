package colesico.framework.security.internal;

import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.security.authentication.AuthenticationPeer;
import colesico.framework.security.authentication.PeerContext;

public class PeerContextImpl implements PeerContext {

    protected final RequestScope requestScope;

    public PeerContextImpl(RequestScope requestScope) {
        this.requestScope = requestScope;
    }

    @Override
    public Iterable<AuthenticationPeer> peers() {
        var peers = requestScope.get(SCOPE_KEY);
        return peers == null ? null : peers.values();
    }

    @Override
    public void setPeers(Iterable<AuthenticationPeer> peers) {
        requestScope.put(SCOPE_KEY, new Peers(peers));
    }

    @Override
    public void clear() {
        requestScope.put(SCOPE_KEY, null);
    }

}
