package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.profile.teleapi.ProfileTeleAssist;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Default profile tele-assistant
 */
@Singleton
public class ProfileTeleAssistImpl implements ProfileTeleAssist<DefaultProfile> {

    @Override
    public byte[] serialize(DefaultProfile profile) {
        StringBuilder sb = new StringBuilder();
        sb.append(profile.getLocale().getLanguage());
        sb.append('|');
        sb.append(profile.getLocale().getCountry());
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DefaultProfile deserialize(byte[] profileBytes) {
        try {
            String localeStr = new String(profileBytes, "UTF-8");
            String[] localeItems = StringUtils.split(localeStr, "|");
            Locale locale = new Locale(localeItems[0], localeItems[1]);
            return new DefaultProfile(locale);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile buildDefault(Locale locale) {
        return new DefaultProfile(locale);
    }

}
