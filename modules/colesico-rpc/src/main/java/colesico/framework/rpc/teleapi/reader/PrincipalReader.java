package colesico.framework.rpc.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcTRContext;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Base64;

@Singleton
public class PrincipalReader implements RpcTeleReader<Principal> {

    protected final PrincipalSerializer principalSerializer;
    protected final Provider<HttpContext> httpContextProv;

    public PrincipalReader(PrincipalSerializer principalSerializer, Provider<HttpContext> httpContextProv) {
        this.principalSerializer = principalSerializer;
        this.httpContextProv = httpContextProv;
    }

    @Override
    public Principal read(RpcTRContext context) {

        RpcRequest request = context.getRequest();

        // Retrieve principal from request header
        byte[] principalBytes = (byte[]) request.getHeaders().get(PrincipalWriter.HEADER_NAME);
        if (principalBytes == null) {
            return null;
        }

        Principal principal = principalSerializer.deserialize(principalBytes);
        return principal;
    }
}
