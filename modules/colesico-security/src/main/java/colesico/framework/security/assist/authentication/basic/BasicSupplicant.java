package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;

public interface BasicSupplicant {

    /**
     *  Extract basic message from source  (http/grpc request, rabbit message, etc.)
     */
    BasicMessage message();

    void challenge(String realm);

    void logout(Identity<?> identity);

}