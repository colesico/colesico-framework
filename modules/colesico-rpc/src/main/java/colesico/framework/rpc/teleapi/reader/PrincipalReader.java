package colesico.framework.rpc.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.rpc.teleapi.RpcTDRContext;
import colesico.framework.security.Principal;
import colesico.framework.security.teleapi.PrincipalSerializer;
import colesico.framework.teleapi.TeleReader;
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
    public Principal read(RpcTDRContext context) {
        HttpRequest request = httpContextProv.get().getRequest();

        // Retrieve principal from http header
        String principalValue = request.getHeaders().get(PrincipalWriter.HEADER_NAME);
        if (StringUtils.isBlank(principalValue)) {
            return null;
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] principalBytes = decoder.decode(principalValue);

        Principal principal = principalSerializer.deserialize(principalBytes);
        return principal;
    }
}
