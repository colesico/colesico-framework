package colesico.framework.security.assist.authentication.basic;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.security.authentication.AuthenticationChallenge;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class BasicAuthConfigPrototype {

    protected Integer maxAuthenticated() {
        return 1000;
    }

    /**
     *  Message digest to hash password
     */
    protected String passwordDigest() {
        return "SHA-256";
    }

    protected AuthenticationChallenge challenge(){
        return new BasicAuthenticationChallenge("Authentication");
    }
}
