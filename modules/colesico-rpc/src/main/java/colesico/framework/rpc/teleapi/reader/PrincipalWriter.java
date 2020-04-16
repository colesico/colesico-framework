package colesico.framework.rpc.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.rpc.teleapi.RpcTDWContext;
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
    public void write(Principal principal, RpcTDWContext context) {
        String principalVal;
        if (principal != null) {
            byte[] principalBytes = principalSerializer.serialize(principal);
            Base64.Encoder encoder = Base64.getEncoder();
            principalVal = encoder.encodeToString(principalBytes);
        } else {
            principalVal = null;
        }

        HttpResponse response = httpContextProv.get().getResponse();
        response.setHeader(HEADER_NAME, principalVal);
    }
}
