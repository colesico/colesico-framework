package colesico.framework.profile.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.ProfileConfigPrototype;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.profile.ValueConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileUtilsImpl implements ProfileUtils<ProfileImpl> {

    protected final Map<Class<?>, ValueConverter<?>> valueConverters = new HashMap<>();

    public ProfileUtilsImpl(Polysupplier<ProfileConfigPrototype> profConfigs) {
        profConfigs.forEach(cfg -> {
                    cfg.forEach(cvb -> valueConverters.put(cvb.valueClass(), cvb.converter()));
                }
        );
    }

    @Override
    public ProfileImpl create(Collection<?> attributes, Collection<?> preferences) {
        ProfileImpl profile = new ProfileImpl();
        attributes.forEach(profile::setAttribute);
        preferences.forEach(profile::setAttribute);
        return profile;
    }

    @Override
    public Collection<?> getPreferences(ProfileImpl profile) {
        return profile.getPreferences().values();
    }

    @Override
    public Collection<?> getAttributes(ProfileImpl profile) {
        return profile.getAttributes().values();
    }

    @Override
    public Map<String, String> toProperties(Collection<?> values) {
        return Map.of();
    }

    @Override
    public Collection<?> fromProperties(Map<String, String> properties) {
        return List.of();
    }

    @Override
    public byte[] serialize(Collection<?> values) {
        values.forEach(
                val->{
                    ValueConverter converter = valueConverters.get()
                }
        );
    }

    @Override
    public Collection<?> deserialize(byte[] values) {
        return List.of();
    }


}
