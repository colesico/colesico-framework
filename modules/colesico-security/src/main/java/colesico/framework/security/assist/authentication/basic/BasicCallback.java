package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationCallback;

public interface BasicCallback extends AuthenticationCallback<Identity, Identity> {
    void onChallenge(String realm);
}
