package colesico.framework.profile.teleapi;

import colesico.framework.profile.Profile;

import java.util.Locale;

public interface ProfileTeleAssist<P extends Profile> {

    byte[] serialize(P profile);

    P deserialize(byte[] profileBytes);

    Profile buildDefault(Locale locale);
}
