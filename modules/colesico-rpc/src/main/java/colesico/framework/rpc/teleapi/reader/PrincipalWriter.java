package colesico.framework.rpc.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.rpc.teleapi.RpcResponse;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Provider;
import java.util.Base64;

public class PrincipalWriter implements RpcTeleWriter<Principal> {

    public static final String HEADER_NAME = "Principal";
    protected final Provider<HttpContext> httpContextProv;
    protected final PrincipalSerializer principalSerializer;

    public PrincipalWriter(Provider<HttpContext> httpContextProv, PrincipalSerializer principalSerializer) {
        this.httpContextProv = httpContextProv;
        this.principalSerializer = principalSerializer;
    }

    @Override
    public void write(Principal principal, RpcTWContext context) {
        byte[] principalBytes;
        if (principal != null) {
            principalBytes = principalSerializer.serialize(principal);
        } else {
            principalBytes = null;
        }
        RpcResponse response = context.getResponse();
        response.getHeaders().put(HEADER_NAME, principalBytes);
    }
}
