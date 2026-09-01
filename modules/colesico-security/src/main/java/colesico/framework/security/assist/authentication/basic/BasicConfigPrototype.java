package colesico.framework.security.assist.authentication.basic;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.util.Base64;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class BasicConfigPrototype {

    public Integer maxAuthenticated() {
        return 1000;
    }

    /**
     * Message digest to hash password
     */
    public String passwordDigest() {
        return "SHA-256";
    }

    public String realm() {
        return "Authentication";
    }
}
