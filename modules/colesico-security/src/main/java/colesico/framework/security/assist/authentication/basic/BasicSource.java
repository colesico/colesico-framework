package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;

public interface BasicSource {

    BasicMessage credentials();

    void challenge(String realm);

    void logout(Identity<?> identity);

}