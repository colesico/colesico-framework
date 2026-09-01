package colesico.framework.telehttp.authentication;

import colesico.framework.security.assist.authentication.basic.BasicAccounts;
import colesico.framework.security.assist.authentication.basic.BasicConfigPrototype;
import colesico.framework.security.assist.authentication.basic.SimpleBasic;
import jakarta.inject.Singleton;

@Singleton
public class HttpBasic extends SimpleBasic {
    public HttpBasic(BasicConfigPrototype config,
                     HttpBasicClient client,
                     BasicAccounts accounts) {
        super(config, client, accounts);
    }
}
