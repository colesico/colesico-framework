package colesico.framework.jjwt;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationCallback;

public interface JwtCallback extends AuthenticationCallback<JwtCallback.Success,> {
    void onLogin(Identity);
}
