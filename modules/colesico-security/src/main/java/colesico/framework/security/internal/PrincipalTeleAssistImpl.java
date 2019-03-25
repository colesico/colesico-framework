package colesico.framework.security.internal;

import colesico.framework.security.DefaultPrincipal;
import colesico.framework.security.teleapi.PrincipalTeleAssist;

import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;

/**
 * Default principal tele-assistant
 */
@Singleton
public class PrincipalTeleAssistImpl implements PrincipalTeleAssist<DefaultPrincipal> {

    @Override
    public byte[] serialize(DefaultPrincipal principal) {
        try {
            return principal.getUID().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DefaultPrincipal deserialize(byte[] principalBytes) {
        try {
            String uid = new String(principalBytes, "UTF-8");
            return new DefaultPrincipal(uid);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
