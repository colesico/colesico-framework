package colesico.framework.example.weblet;

import colesico.framework.security.Identity;
import colesico.framework.security.assist.authentication.basic.BasicAuth;
import colesico.framework.security.authentication.Authentication;

import colesico.framework.weblet.Weblet;

import jakarta.inject.Provider;

@Weblet
public class SecuredWeblet {

    private final Provider<Identity<String>> identity;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public SecuredWeblet(Provider<Identity> identity) {
        this.identity = (Provider) identity;
    }

    // Browse: http://localhost:8080/secured-weblet/identity
    // Use admin/secret to  authenticate (see resources/META-INF/accounts.properties)
    @Authentication(BasicAuth.class)
    public String identity() {
        return "Hello, "+ identity.get().id();
    }

}
